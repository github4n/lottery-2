package com.youge.yogee.interfaces.lottery.basketball;

import com.youge.yogee.common.utils.StringUtils;
import com.youge.yogee.interfaces.lottery.football.FootballSingleOrderInterface;
import com.youge.yogee.interfaces.util.HttpResultUtil;
import com.youge.yogee.interfaces.util.HttpServletRequestUtils;
import com.youge.yogee.interfaces.util.util;
import com.youge.yogee.modules.cbasketballmixed.entity.CdBasketballMixed;
import com.youge.yogee.modules.cbasketballmixed.service.CdBasketballMixedService;
import com.youge.yogee.modules.cbasketballorder.entity.CdBasketballSingleOrder;
import com.youge.yogee.modules.cbasketballorder.service.CdBasketballSingleOrderService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyifeng on 2018-02-26.
 */
@Controller
@RequestMapping("${frontPath}")
public class BasketballSingleOrderInterface {

    private static final Logger logger = LoggerFactory.getLogger(FootballSingleOrderInterface.class);
    @Autowired
    private CdBasketballMixedService cdBasketballMixedService;
    @Autowired
    private CdBasketballSingleOrderService cdBasketballSingleOrderService;

    /**
     * 篮球单关 提交订单
     */
    @RequestMapping(value = "basketballSingleOrderCommit", method = RequestMethod.POST)
    @ResponseBody
    public String basketballSingleOrderCommit(HttpServletRequest request) {
        logger.info(" interface basketballSingleOrderCommit--------Start-------------------");
        logger.debug("interface 请求--basketballSingleOrderCommit-------- Start--------");
        Map map = new HashMap();
        Map jsonData = HttpServletRequestUtils.readJsonData(request);
        if (jsonData == null) {
            return HttpResultUtil.errorJson("json格式错误");
        }
//        //玩法 1混投 2胜负平 3猜比分 4总进球 5半全场 6让球
//        String buyWays = (String) jsonData.get("buyWays");
//        if (StringUtils.isEmpty(buyWays)) {
//            logger.error("buyWays为空");
//            return HttpResultUtil.errorJson("buyWays为空");
//        }
        //用户id
        String uid = (String) jsonData.get("uid");
        if (StringUtils.isEmpty(uid)) {
            logger.error("uid为空");
            return HttpResultUtil.errorJson("uid为空");
        }

        //订单详情
        Object jsonString = jsonData.get("detail");
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        List<Map<String, Object>> detail = (List<Map<String, Object>>) jsonArray.toCollection(jsonArray, Map.class);
        //List<String> resultList = new ArrayList<>();
        int acount = 0;//注数
        String winDetail = "";//主胜详情
        String failDetail = "";//主负详情
        if (detail.size() != 0) {
            for (Map<String, Object> d : detail) {

                int partCount = 0;
                String matchId = (String) d.get("matchId");
                CdBasketballMixed cbm = cdBasketballMixedService.findByMatchId(matchId);
                if (cbm == null) {
                    return HttpResultUtil.errorJson("比赛不存在");
                }
                String partDetail = matchId + "+" + cbm.getWinningName() + "vs" + cbm.getDefeatedName();

                //主胜分差
                String hostWin = (String) d.get("hostWin");
                if (StringUtils.isNotEmpty(hostWin)) {
                    String winArray[] = hostWin.split(",");
                    for (String s : winArray) {
                        String aWin[] = s.split("/");
                        String count = aWin[2];
                        int countInt = Integer.parseInt(count);
                        partCount += countInt;
                    }
                    //partDetail += "+" + hostWin;
                    winDetail += partDetail + "+" + hostWin + "|";
                }

                //主负分差
                String hostFail = (String) d.get("hostFail");
                if (StringUtils.isNotEmpty(hostFail)) {
                    String failArray[] = hostFail.split(",");
                    for (String s : failArray) {
                        String aFail[] = s.split("/");
                        String count = aFail[2];
                        int countInt = Integer.parseInt(count);
                        partCount += countInt;
                    }
//                    partDetail += "+" + hostFail;
                    failDetail += partDetail + "+" + hostFail + "|";
                }


                acount += partCount;
//                orderDetail += partDetail + "|";

            }
        }


        //注数
        String acountStr = String.valueOf(acount);
        //生成订单号
        String orderNum = util.genOrderNo("LDG", util.getFourRandom());
        //计算金额
        double money = 2.00;
        double acountDouble = Double.parseDouble(acountStr);
        String price = String.valueOf(money * acountDouble);

        CdBasketballSingleOrder cbso = new CdBasketballSingleOrder();

        cbso.setOrderNum(orderNum); //订单号
        cbso.setAcount(acountStr);//注数
        cbso.setAward("0"); //奖金
        //cbso.setOrderDetail(orderDetail); //订单详情
        cbso.setHostWin(winDetail);
        cbso.setHostFail(failDetail);
        cbso.setPrice(price);//金额
        cbso.setStatus("1");//已提交
        cbso.setUid(uid);//用户
        cbso.setBuyWays("1"); //玩法 1混投
        try {
            cdBasketballSingleOrderService.save(cbso);
            map.put("flag", "1");
        } catch (Exception e) {
            return HttpResultUtil.errorJson("保存失败");
        }

        logger.info("footballSingleOrderCommit---------End---------------------");
        return HttpResultUtil.successJson(map);
    }

}