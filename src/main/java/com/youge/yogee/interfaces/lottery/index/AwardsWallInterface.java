package com.youge.yogee.interfaces.lottery.index;

import com.youge.yogee.common.utils.StringUtils;
import com.youge.yogee.interfaces.util.BallGameCals;
import com.youge.yogee.interfaces.util.HttpResultUtil;
import com.youge.yogee.interfaces.util.HttpServletRequestUtils;
import com.youge.yogee.modules.cbasketballorder.entity.CdBasketballFollowOrder;
import com.youge.yogee.modules.cbasketballorder.entity.CdBasketballSingleOrder;
import com.youge.yogee.modules.cbasketballorder.service.CdBasketballFollowOrderService;
import com.youge.yogee.modules.cbasketballorder.service.CdBasketballSingleOrderService;
import com.youge.yogee.modules.cchoosenine.entity.CdChooseNineOrder;
import com.youge.yogee.modules.cchoosenine.service.CdChooseNineOrderService;
import com.youge.yogee.modules.cfiveawards.entity.CdFiveOrder;
import com.youge.yogee.modules.cfiveawards.service.CdFiveOrderService;
import com.youge.yogee.modules.cfootballorder.entity.CdFootballFollowOrder;
import com.youge.yogee.modules.cfootballorder.entity.CdFootballSingleOrder;
import com.youge.yogee.modules.cfootballorder.service.CdFootballFollowOrderService;
import com.youge.yogee.modules.cfootballorder.service.CdFootballSingleOrderService;
import com.youge.yogee.modules.clotteryuser.entity.CdLotteryUser;
import com.youge.yogee.modules.clotteryuser.service.CdLotteryUserService;
import com.youge.yogee.modules.clottoreward.entity.CdLottoOrder;
import com.youge.yogee.modules.clottoreward.service.CdLottoOrderService;
import com.youge.yogee.modules.corder.entity.CdOrder;
import com.youge.yogee.modules.corder.entity.CdOrderWinners;
import com.youge.yogee.modules.corder.service.CdOrderService;
import com.youge.yogee.modules.corder.service.CdOrderWinnersService;
import com.youge.yogee.modules.csuccessfail.entity.CdSuccessFailOrder;
import com.youge.yogee.modules.csuccessfail.service.CdSuccessFailOrderService;
import com.youge.yogee.modules.cthreeawards.entity.CdThreeOrder;
import com.youge.yogee.modules.cthreeawards.service.CdThreeOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wjc on 2017-12-21 0021.
 */
@Controller
@RequestMapping("${frontPath}")
public class AwardsWallInterface {
    private static final Logger logger = LoggerFactory.getLogger(AwardsWallInterface.class);
    @Autowired
    private CdOrderWinnersService cdOrderWinnersService;
    @Autowired
    private CdLotteryUserService cdLotteryUserService;
    @Autowired
    private CdBasketballSingleOrderService cdBasketballSingleOrderService;  //篮球单关
    @Autowired
    private CdBasketballFollowOrderService cdBasketballFollowOrderService;//篮球串关
    @Autowired
    private CdFootballFollowOrderService cdFootballFollowOrderService;//足球串关
    @Autowired
    private CdFootballSingleOrderService cdFootballSingleOrderService;//足球单关
    @Autowired
    private CdThreeOrderService cdThreeOrderService;
    @Autowired
    private CdFiveOrderService cdFiveOrderService;
    @Autowired
    private CdLottoOrderService cdLottoOrderService;
    @Autowired
    private CdChooseNineOrderService cdChooseNineOrderService;
    @Autowired
    private CdSuccessFailOrderService cdSuccessFailOrderService;
    @Autowired
    private CdOrderService cdOrderService;


    /**
     * 大奖墙列表接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "awardsWallList")
    @ResponseBody
    public String awardsWallList(HttpServletRequest request) {
        logger.info("大奖墙列表接口--------------Start-----");
        Map dataMap = new HashMap();
        List<CdOrderWinners> list = cdOrderWinnersService.findByWallType("1");
        List<Map<String, String>> textStr = new ArrayList<>();
        if (list.size() > 0) {
            for (CdOrderWinners awards : list) {
                Map<String, String> map = new HashMap<>();
                CdLotteryUser clu = cdLotteryUserService.get(awards.getUid());
                String name = clu.getName();
                String result = getWinType(awards.getType());
                String winPrice = awards.getWinPrice();
                String repayPercent = awards.getRepayPercent() + "%";
                String date = awards.getCreateDate().split(" ")[0];
                //String text = name + "恭喜您，您自购的" + result + "喜中" + winPrice + "元奖金，您的回报率为" + repayPercent + "%，特此表彰，以资鼓励！\n" + date;
                String text1 = "恭喜您，您自购的" + result + "喜中";
                String text2 = "元奖金，您的回报率为";
                String text3 = "，特此表彰，以资鼓励！";
                map.put("id", awards.getId()); //用户名
                map.put("name", name); //用户名
                map.put("text1", text1); //前半句
                map.put("winPrice", winPrice); //奖金
                map.put("text2", text2); //中间句
                map.put("repayPercent", repayPercent); //回报率
                map.put("text3", text3); //后半句
                map.put("date", date); //时间
                textStr.add(map);
            }
        }
        dataMap.put("list", textStr);
        logger.info("大奖墙列表接口--------------End--------");
        return HttpResultUtil.successJson(dataMap);
    }


    /**
     * 大奖墙详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "awardsWallDetail")
    @ResponseBody
    public String awardsWallDetail(HttpServletRequest request) {
        logger.info("大奖墙详情--------------Start-----");
        Map jsonData = HttpServletRequestUtils.readJsonData(request);
        Map map = new HashMap();
        //id
        String wid = (String) jsonData.get("wid");
        if (StringUtils.isEmpty(wid)) {
            logger.error("wid为空！");
            return HttpResultUtil.errorJson("wid为空!");
        }
        //1大奖墙详情  2我的订单详情
        String type = (String) jsonData.get("type");
        if (StringUtils.isEmpty(type)) {
            logger.error("type为空！");
            return HttpResultUtil.errorJson("type为空!");
        }
        String orderNum = "";
        if ("1".equals(type)) {
            CdOrderWinners cow = cdOrderWinnersService.get(wid);
            if (cow == null) {
                return HttpResultUtil.errorJson("信息错误！");
            }
            CdLotteryUser clu = cdLotteryUserService.get(cow.getUid());
            map.put("name", clu.getName());
            map.put("img", clu.getImg());
            map.put("winPrice", cow.getWinPrice());
            map.put("type", cow.getType());//1足球单关 2足球串关 3篮球单关 4篮球串关 5任选九 6胜负彩 7排列三 8排列五 9大乐透
            orderNum = cow.getWinOrderNum();
        } else {
            CdOrder co = cdOrderService.get(wid);
            if (co == null) {
                return HttpResultUtil.errorJson("信息错误！");
            }
            CdLotteryUser clu = cdLotteryUserService.get(co.getUserId());
            map.put("name", clu.getName());
            map.put("img", clu.getImg());
            map.put("winPrice", co.getWinPrice());
            map.put("type", co.getType());//1足球单关 2足球串关 3篮球单关 4篮球串关 5任选九 6胜负彩 7排列三 8排列五 9大乐透
            orderNum = co.getNumber();
        }

        if (orderNum.startsWith("ZDG")) {
            CdFootballSingleOrder cfs = cdFootballSingleOrderService.findOrderByOrderNum(orderNum);
            List detailList = getFbSingleList(cfs);
            map.put("buyWays", cfs.getBuyWays());
            map.put("followNums", "0");
            map.put("detail", detailList);
            map.put("buyWays", cfs.getBuyWays());
        } else if (orderNum.startsWith("ZCG")) {
            CdFootballFollowOrder cff = cdFootballFollowOrderService.findOrderByOrderNum(orderNum);
            List detailList = getFbFollowList(cff);
            map.put("buyWays", cff.getBuyWays());
            map.put("followNums", cff.getFollowNum());
            map.put("detail", detailList);
            map.put("buyWays", cff.getBuyWays());
        } else if (orderNum.startsWith("LDG")) {
            CdBasketballSingleOrder cbs = cdBasketballSingleOrderService.findOrderByOrderNum(orderNum);
            List detailList = getBbSingleList(cbs);
            map.put("followNums", "0");
            map.put("buyWays", cbs.getBuyWays());
            map.put("detail", detailList);
            map.put("buyWays", cbs.getBuyWays());
        } else if (orderNum.startsWith("LCG")) {
            CdBasketballFollowOrder cbf = cdBasketballFollowOrderService.findOrderByOrderNum(orderNum);
            List detailList = getBbFollowList(cbf);
            map.put("buyWays", cbf.getBuyWays());
            map.put("followNums", cbf.getFollowNums());
            map.put("detail", detailList);
            map.put("buyWays", cbf.getBuyWays());
        } else if (orderNum.startsWith("RXJ")) {
            CdChooseNineOrder ccno = cdChooseNineOrderService.findOrderByOrderNum(orderNum);
            map.put("price", ccno.getPrice());//投注金额
            String orderDetail = ccno.getOrderDetail(); //投注内容
            String[] orderDetailArray = orderDetail.split("\\|");
            String result = ccno.getResult();  //比赛结果
            List<String> rList = new ArrayList<>();
            if (StringUtils.isNotEmpty(result)) {
                for (String s : result.split(",")) {
                    rList.add(s);
                }
            }
            List detailList = new ArrayList();
            for (String aOrderDetail : orderDetailArray) {
                Map detailMap = getDetailMap(aOrderDetail, rList);
                detailList.add(detailMap);
            }
            map.put("detail", detailList);
        } else if (orderNum.startsWith("SFC")) {
            CdSuccessFailOrder csfo = cdSuccessFailOrderService.findOrderByOrderNum(orderNum);
            map.put("price", csfo.getPrice());//投注金额
            String orderDetail = csfo.getOrderDetail(); //投注内容
            String[] orderDetailArray = orderDetail.split("\\|");
            String result = csfo.getResult();  //比赛结果
            List<String> rList = new ArrayList<>();
            if (StringUtils.isNotEmpty(result)) {
                for (String s : result.split(",")) {
                    rList.add(s);
                }
            }
            List detailList = new ArrayList();
            for (String aOrderDetail : orderDetailArray) {
                Map detailMap = getDetailMap(aOrderDetail, rList);
                detailList.add(detailMap);
            }
            map.put("detail", detailList);
        } else if (orderNum.startsWith("PLS")) {
            CdThreeOrder dto = cdThreeOrderService.findOrderByOrderNum(orderNum);
            map.put("buyWays", dto.getBuyWays());//排列三玩法
            map.put("codes", dto.getNums()); //押注详情
            map.put("mResult", dto.getResult());//中奖结果
            map.put("price", dto.getPrice());//投注金额
        } else if (orderNum.startsWith("PLW")) {
            CdFiveOrder cfo = cdFiveOrderService.findOrderByOrderNum(orderNum);
            map.put("buyWays", cfo.getBuyWays());//排列五玩法
            map.put("codes", cfo.getNums()); //押注详情
            map.put("mResult", cfo.getResult());//中奖结果
            map.put("price", cfo.getPrice());//投注金额
        } else if (orderNum.startsWith("DLT")) {
            CdLottoOrder clo = cdLottoOrderService.findOrderByOrderNum(orderNum);
            String nums = clo.getRedNums() + "|" + clo.getBlueNums();
            map.put("buyWays", clo.getType());//大乐透方式胆拖/普通
            map.put("codes", nums); //押注详情
            map.put("mResult", clo.getResult());//中奖结果
            map.put("price", clo.getPrice());//投注金额
        }
        logger.info("大奖墙列表接口--------------End--------");
        return HttpResultUtil.successJson(map);
    }


    public String getWinType(String type) {
        String result = "";
        int i = Integer.parseInt(type);
        switch (i) {
            case 1:
                result = "足球单关";
                break;
            case 2:
                result = "足球串关";
                break;
            case 3:
                result = "篮球单关";
                break;
            case 4:
                result = "篮球串关";
                break;
            case 5:
                result = "任选九";
                break;
            case 6:
                result = "胜负彩";
                break;
            case 7:
                result = "排列三";
                break;
            case 8:
                result = "排列五";
                break;
            case 9:
                result = "大乐透";
                break;
        }
        return result;
    }

    public Map getDetailMap(String aOrderDetail, List<String> rList) {
        int i = 0;
        String[] aOrderDetailArray = aOrderDetail.split("\\+");
        Map detailMap = new HashMap();
        detailMap.put("no", aOrderDetailArray[0]);//序号
        detailMap.put("vs", aOrderDetailArray[1]);//比赛队伍
        //detailMap.put("codes", aOrderDetailArray[2]);//投注详情
        String aDetail = aOrderDetailArray[2];
        String newAdetail = aDetail.replace("3", "胜");
        newAdetail = newAdetail.replace("1", "平");
        newAdetail = newAdetail.replace("0", "负");
        detailMap.put("codes", newAdetail);//投注详情
        String mResult = "";
        if (rList.size() > 0) {
            mResult = rList.get(i);
            if ("3".equals(mResult)) {
                mResult = "胜";
            } else if ("1".equals(mResult)) {
                mResult = "平";
            } else if ("0".equals(mResult)) {
                mResult = "负";
            } else {
                mResult = "*";
            }
            i++;
        }
        detailMap.put("mResult", mResult);//投注详情

        return detailMap;
    }

    public static Map<String, String> getSingleMap(String s, String[] strArray) {
        Map<String, String> map = new HashMap<>();
        if (strArray.length > 0) {
            for (String aStr : strArray) {
                String[] aStrArray = aStr.split("\\+");
                if (s.equals(aStrArray[0])) {
                    map.put("vs", aStrArray[1]);
                    map.put("result", aStrArray[2]);
                }
            }
        }
        return map;
    }

    public static Map<String, String> getFollowMap(String s, String[] strArray) {
        Map<String, String> map = new HashMap<>();
        if (strArray.length > 0) {
            for (String aStr : strArray) {
                String[] aStrArray = aStr.split("\\+");
                if (s.equals(aStrArray[1])) {
                    map.put("vs", aStrArray[2]);
                    map.put("result", aStrArray[3]);
                }
            }
        }
        return map;
    }

    public static List getFbFollowList(CdFootballFollowOrder cff) {
        List detailList = new ArrayList();
        //比分
        String score = cff.getScore();
        String[] scoreArray = score.split("\\|");
        //进球
        String goal = cff.getGoal();
        String[] goalArray = goal.split("\\|");
        //半全场
        String half = cff.getHalf();
        String[] halfArray = half.split("\\|");
        //胜负
        String beat = cff.getBeat();
        String[] beatArray = beat.split("\\|");
        //让球
        String let = cff.getLet();
        String[] letArray = let.split("\\|");
        //所有比赛
        String matchIds = cff.getDanMatchIds();
        String[] matchIdsArray = matchIds.split(",");
        String vs = "";
        String matchResult = cff.getResult();//比赛结果
        int i = 0;
        for (String s : matchIdsArray) {
            List<String> resultList = new ArrayList<>();
            if (StringUtils.isNotEmpty(matchResult)) {
                String[] mathchResultArray = matchResult.split(",");
                for (String rs : mathchResultArray) {
                    resultList.add(rs);
                }
            }

            String match = s.split("\\+")[1];
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("matchId", match);
            //比赛结果
            if (resultList.size() > 0) {
                orderMap.put("result", resultList.get(i));
                i++;
            } else {
                orderMap.put("result", "");
            }

            //比分
            Map<String, String> scoreMap = getFollowMap(match, scoreArray);
            if (scoreMap.size() > 0) {
                vs = scoreMap.get("vs");
            }
            orderMap.put("score", scoreMap.get("result"));
            //进球
            Map<String, String> goalMap = getFollowMap(match, goalArray);
            if (goalMap.size() > 0) {
                vs = goalMap.get("vs");
            }
            orderMap.put("goal", goalMap.get("result"));
            //半全场
            String halfName = "";
            Map<String, String> halfMap = getFollowMap(match, halfArray);
            if (halfMap.size() > 0) {
                vs = halfMap.get("vs");
                Map<String, String> nameMap = BallGameCals.getHalfWholeNames();
                String result = halfMap.get("result");
                String resultArray[] = result.split(",");
                for (String r : resultArray) {
                    String wholeResult = "";
                    String[] rArray = r.split("/");
                    wholeResult = nameMap.get(rArray[0]) + "/" + rArray[1];
                    halfName += wholeResult + ",";
                }
            }
            orderMap.put("half", halfName); //半全场
            //胜负平
            Map<String, String> beatMap = getFollowMap(match, beatArray);
            if (beatMap.size() > 0) {
                vs = beatMap.get("vs");
            }
            orderMap.put("beat", beatMap.get("result"));
            //让球
            Map<String, String> letMap = getFollowMap(match, letArray);
            if (letMap.size() > 0) {
                vs = letMap.get("vs");
            }
            orderMap.put("let", letMap.get("result"));
            orderMap.put("vs", vs);

            detailList.add(orderMap);
        }
        return detailList;
    }

    public static List getFbSingleList(CdFootballSingleOrder cfs) {
        List detailList = new ArrayList();
        //比分
        String score = cfs.getScore();
        String[] scoreArray = score.split("\\|");
        //进球
        String goal = cfs.getGoal();
        String[] goalArray = goal.split("\\|");
        //半全场
        String half = cfs.getHalf();
        String[] halfArray = half.split("\\|");
        //胜负
        String beat = cfs.getBeat();
        String[] beatArray = beat.split("\\|");
        //让球
        String let = cfs.getLet();
        String[] letArray = let.split("\\|");
        //所有比赛
        String matchIds = cfs.getMatchIds();
        String[] matchIdsArray = matchIds.split(",");
        String vs = "";
        String matchResult = cfs.getResult();//比赛结果
        int i = 0;
        for (String s : matchIdsArray) {
            List<String> resultList = new ArrayList<>();
            if (StringUtils.isNotEmpty(matchResult)) {
                String[] mathchResultArray = matchResult.split(",");
                for (String rs : mathchResultArray) {
                    resultList.add(rs);
                }
            }

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("matchId", s); //期次
            //比赛结果
            if (resultList.size() > 0) {
                orderMap.put("result", resultList.get(i));
                i++;
            } else {
                orderMap.put("result", "");
            }
            //比分
            Map<String, String> scoreMap = getSingleMap(s, scoreArray);
            if (scoreMap.size() > 0) {
                vs = scoreMap.get("vs"); //队伍
            }
            orderMap.put("score", scoreMap.get("result")); //比分
            //进球
            Map<String, String> goalMap = getSingleMap(s, goalArray);
            if (goalMap.size() > 0) {
                vs = goalMap.get("vs");
            }
            orderMap.put("goal", goalMap.get("result")); //进球
            //半全场
            String halfName = "";
            Map<String, String> halfMap = getSingleMap(s, halfArray);
            if (halfMap.size() > 0) {
                vs = halfMap.get("vs");
                Map<String, String> nameMap = BallGameCals.getHalfWholeNames();
                String result = halfMap.get("result");
                String resultArray[] = result.split(",");
                for (String r : resultArray) {
                    String wholeResult = "";
                    String[] rArray = r.split("/");
                    wholeResult = nameMap.get(rArray[0]) + "/" + rArray[1];
                    halfName += wholeResult + ",";
                }
            }
            orderMap.put("half", halfName); //半全场
            //胜负平
            Map<String, String> beatMap = getSingleMap(s, beatArray);
            if (beatMap.size() > 0) {
                vs = beatMap.get("vs");
            }
            orderMap.put("beat", beatMap.get("result")); //胜负
            //让球
            Map<String, String> letMap = getSingleMap(s, letArray);
            if (letMap.size() > 0) {
                vs = letMap.get("vs");
            }
            orderMap.put("let", letMap.get("result")); //让球
            orderMap.put("vs", vs);
            detailList.add(orderMap);
        }
        return detailList;
    }

    public static List getBbSingleList(CdBasketballSingleOrder cbs) {
        List detailList = new ArrayList();
        //主胜
        String hostWin = cbs.getHostWin();
        String[] winArray = hostWin.split("\\|");
        //主负
        String hostFail = cbs.getHostFail();
        String[] failArray = hostFail.split("\\|");

        //所有比赛
        String matchIds = cbs.getMatchIds();
        String[] matchIdsArray = matchIds.split(",");
        String vs = "";
        String matchResult = cbs.getResult();//比赛结果
        int i = 0;
        for (String s : matchIdsArray) {

            List<String> resultList = new ArrayList<>();
            if (StringUtils.isNotEmpty(matchResult)) {
                String[] mathchResultArray = matchResult.split(",");
                for (String rs : mathchResultArray) {
                    resultList.add(rs);
                }
            }

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("matchId", s); //期次
            //比赛结果
            if (resultList.size() > 0) {
                orderMap.put("result", resultList.get(i));
                i++;
            } else {
                orderMap.put("result", "");
            }
            //主胜
            Map<String, String> winMap = getSingleMap(s, winArray);
            if (winMap.size() > 0) {
                vs = winMap.get("vs"); //队伍
            }

            orderMap.put("win", winMap.get("result")); //比分
            //主负
            Map<String, String> failMap = getSingleMap(s, failArray);
            if (failMap.size() > 0) {
                vs = failMap.get("vs");
            }
            orderMap.put("fail", failMap.get("result")); //进球
            orderMap.put("beat", "");
            orderMap.put("let", "");
            orderMap.put("size", "");
            orderMap.put("vs", vs);
            //map.put("result", cbs.getResult());//彩果
            detailList.add(orderMap);
        }
        return detailList;
    }

    public static List getBbFollowList(CdBasketballFollowOrder cbf) {
        List detailList = new ArrayList();
        //主胜
        String hostWin = cbf.getHostWin();
        String[] winArray = hostWin.split("\\|");
        //主负
        String hostFail = cbf.getHostFail();
        String[] failArray = hostFail.split("\\|");
        //胜负
        String beat = cbf.getBeat();
        String[] beatArray = beat.split("\\|");
        //让球胜负
        String let = cbf.getBeat();
        String[] letArray = let.split("\\|");
        //大小分
        String size = cbf.getSize();
        String[] sizeArray = size.split("\\|");
        //所有比赛
        String matchIds = cbf.getDanMatchIds();
        String[] matchIdsArray = matchIds.split(",");
        String vs = "";
        String matchResult = cbf.getResult();//比赛结果
        int j = 0;
        for (String s : matchIdsArray) {
            List<String> resultList = new ArrayList<>();
            if (StringUtils.isNotEmpty(matchResult)) {
                String[] mathchResultArray = matchResult.split(",");
                for (String rs : mathchResultArray) {
                    resultList.add(rs);
                }
            }

            String match = s.split("\\+")[1];
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("matchId", match);
            //比赛结果
            if (resultList.size() > 0) {
                orderMap.put("result", resultList.get(j));
                j++;
            } else {
                orderMap.put("result", "");
            }
            //主胜
            Map<String, String> winMap = getFollowMap(match, winArray);
            if (winMap.size() > 0) {
                vs = winMap.get("vs");
            }
            orderMap.put("win", winMap.get("result"));
            //主负
            Map<String, String> failMap = getFollowMap(match, failArray);
            if (failMap.size() > 0) {
                vs = failMap.get("vs");
            }
            orderMap.put("fail", failMap.get("result"));
            //胜负
            Map<String, String> beatMap = getFollowMap(match, beatArray);
            if (beatMap.size() > 0) {
                vs = beatMap.get("vs");
            }
            orderMap.put("beat", beatMap.get("result"));
            //让球胜负
            Map<String, String> letMap = getFollowMap(match, letArray);
            if (letMap.size() > 0) {
                vs = letMap.get("vs");
            }
            orderMap.put("let", letMap.get("result"));
            //大小分
            String finalSize = "";
            if (sizeArray.length > 0) {
                String sizeCount = cbf.getSizeCount();
                String[] sizeCountArray = sizeCount.split(",");
                for (int i = 0; i < sizeCountArray.length; i++) {
                    String[] aSizeArray = sizeArray[i].split("\\+");
                    if (s.split("\\+")[1].equals(aSizeArray[1])) {
                        orderMap.put("vs", aSizeArray[2]);
                        String sizeResult = aSizeArray[3];
                        String[] sizeResultArray = sizeResult.split(",");
                        for (String r : sizeResultArray) {
                            Map<String, String> sizeName = BallGameCals.getSizeNames();
                            String wholeResult = "";
                            String[] rArray = r.split("/");
                            wholeResult = sizeName.get(rArray[0]) + sizeCountArray[i] + "/" + rArray[1];
                            finalSize += wholeResult + ";";
                        }
                        orderMap.put("size", finalSize);
                    }
                }
            }

            detailList.add(orderMap);
        }
        return detailList;
    }
}
