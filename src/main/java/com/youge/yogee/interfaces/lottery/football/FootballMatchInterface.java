package com.youge.yogee.interfaces.lottery.football;

import com.youge.yogee.interfaces.util.HttpResultUtil;
import com.youge.yogee.interfaces.util.HttpServletRequestUtils;
import com.youge.yogee.modules.cfbalreadyfinsh.entity.CdFbAlreadyFinsh;
import com.youge.yogee.modules.cfbalreadyfinsh.service.CdFbAlreadyFinshService;
import com.youge.yogee.modules.cfbfinshed.entity.CdFbFinshed;
import com.youge.yogee.modules.cfbfinshed.service.CdFbFinshedService;
import com.youge.yogee.modules.cftlogo.entity.CdFtLogo;
import com.youge.yogee.modules.cftlogo.service.CdFtLogoService;
import com.youge.yogee.modules.cftskill.entity.CdFtSkill;
import com.youge.yogee.modules.cftskill.service.CdFtSkillService;
import com.youge.yogee.modules.csceneecharts.entity.CdSceneEcharts;
import com.youge.yogee.modules.csceneecharts.service.CdSceneEchartsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liyuan
 * @
 * Created by wjc on 2018-1-23 0023.
 */
@Controller
@RequestMapping("${frontPath}")
public class FootballMatchInterface {
    private static final Logger logger = LoggerFactory.getLogger(FootballMatchInterface.class);

    @Autowired
    private CdFbFinshedService cdFbFinshedService;
    @Autowired
    private CdFbAlreadyFinshService cdFbAlreadyFinshService;
    @Autowired
    private CdSceneEchartsService cdSceneEchartsService;
    @Autowired
    private CdFtSkillService cdFtSkillService;
    @Autowired
    private CdFtLogoService cdFtLogoService;

    /**
     * 未完赛数据接口
     *
     * @param
     */
    @RequestMapping(value = "notFinshedData", method = RequestMethod.POST)
    @ResponseBody
    public String notFinshedData(HttpServletRequest request) {
        logger.info("notFinshedData  未完赛数据---------Start---------");
        List list = new ArrayList();
        List<CdFbFinshed> dataList = cdFbFinshedService.getNotFinshed();
        for (CdFbFinshed str : dataList) {
            Map<String,Object> map = new HashMap<>();
            map.put("qc", str.getQc());
            map.put("sort", str.getSort());//赛事名称
            map.put("type", str.getType());//比赛时间
            map.put("ln", str.getLn());//赛事类型
            map.put("hn", str.getHn());//主队
            map.put("gn", str.getGn());//客队
            List<CdFtLogo> hnList = cdFtLogoService.findLogo(str.getHn());
            List<CdFtLogo> hnList1 = cdFtLogoService.findLogo(str.getGn());
            map.put("hnLogo", hnList.size() > 0 ? hnList.get(0).getTeamLogo() : ""); //主队图标
            map.put("gnLogo", hnList1.size() > 0 ? hnList.get(0).getTeamLogo() : ""); //客队图标
            map.put("jn", str.getJn());//平赔率
            map.put("time", str.getTime());//比赛时间
            list.add(map);
        }
        Map dataMap = new HashMap();
        dataMap.put("list", list);
        logger.info("notFinshedData  未完赛数据---------End---------");
        return HttpResultUtil.successJson(dataMap);
    }

    /**
     * 已完赛数据接口
     *
     * @param
     */
    @RequestMapping(value = "finshedData", method = RequestMethod.POST)
    @ResponseBody
    public String finshedData(HttpServletRequest request) {
        logger.info("finshedData  已完赛数据---------Start---------");
        List list = new ArrayList();
        List<CdFbAlreadyFinsh> dataList = cdFbAlreadyFinshService.getAlreadyFinsh();
        for (CdFbAlreadyFinsh str : dataList) {
            Map map = new HashMap();
            map.put("qc", str.getQc());
            map.put("sort", str.getSort());//赛事名称
            map.put("type", str.getType());//比赛时间
            map.put("ln", str.getLn());//赛事类型
            map.put("hn", str.getHn());//主队
            map.put("gn", str.getGn());//客队
            List<CdFtLogo> hnList = cdFtLogoService.findLogo(str.getHn());
            List<CdFtLogo> hnList1 = cdFtLogoService.findLogo(str.getGn());
            map.put("hnLogo", hnList.size() > 0 ? hnList.get(0).getTeamLogo() : ""); //主队图标
            map.put("gnLogo", hnList1.size() > 0 ? hnList.get(0).getTeamLogo() : ""); //客队图标
            map.put("jn", str.getJn());//平赔率
            map.put("time", str.getTime());//比赛时间
            list.add(map);
        }
        Map dataMap = new HashMap();
        dataMap.put("list", list);
        logger.info("finshedData  已完赛数据---------End---------");
        return HttpResultUtil.successJson(dataMap);
    }

    /**
     * 足球实况(比赛事件echarts图表)
     */
    @RequestMapping(value = "ftEcharts", method = RequestMethod.POST)
    @ResponseBody
    public String ftEcharts(HttpServletRequest request) {
        Map jsonData = HttpServletRequestUtils.readJsonData(request);
        String itemId = (String) jsonData.get("itemId");
        logger.info("ftEcharts  比赛事件echarts图表---------Start---------");
        List list = new ArrayList();
        List<CdSceneEcharts> dataList = cdSceneEchartsService.getEcharts(itemId);
        for (CdSceneEcharts str : dataList) {
            Map map = new HashMap();
            map.put("pn", str.getPn()); //
            map.put("eventType", str.getEventType());//类型(0进球3黄牌5红牌)
            map.put("teamType", str.getTeamType());
            map.put("itemId", str.getItemId());
            map.put("hn", str.getHn());//主队
            map.put("gn", str.getGn());//客队
            map.put("isFinshed", str.getIsFinshed());//是否完赛
            map.put("time", str.getTime());//比赛时间
            list.add(map);
        }
        Map dataMap = new HashMap();
        dataMap.put("list", list);
        logger.info("ftEcharts  比赛事件echarts图表---------End---------");
        return HttpResultUtil.successJson(dataMap);
    }

    /**
     * 足球实况(技术统计 首发名单和替补名单)
     */
    @RequestMapping(value = "ftBallSkill", method = RequestMethod.POST)
    @ResponseBody
    public String ftBallSkill(HttpServletRequest request) {
        logger.info("ftBallSkill  足球实况(技术统计 首发名单和替补名单)---------Start---------");
        Map jsonData = HttpServletRequestUtils.readJsonData(request);
        String itemId = (String) jsonData.get("itemId");
        List list = new ArrayList();
        List<CdFtSkill> dataList = cdFtSkillService.getFtSkill(itemId);
        for (CdFtSkill str : dataList) {
            Map map = new HashMap();
            map.put("itemId", str.getItemId());
            map.put("shootnuma", str.getShootnuma()); //射门a
            map.put("shotsongoala", str.getShotsongoala()); //射正a
            map.put("foulnuma", str.getFoulnuma()); //犯规A
            map.put("hn", str.getHn());//主队
            map.put("gn", str.getGn());//客队
            map.put("isFinshed", str.getIsFinshed());//是否完赛
            map.put("cornerkicknuma", str.getCornerkicknuma());//角球A
            map.put("offsidenuma", str.getOffsidenuma());//越位A
            map.put("yellowcardnuma", str.getYellowcardnuma());//黄牌A
            map.put("savesa", str.getSavesa());//救球
            map.put("time", str.getPlayera());//主队名单
            map.put("playera", str.getTbplayera());//替补A
            map.put("shootnumb", str.getShootnumb()); //射门b
            map.put("shotsongoalb", str.getShotsongoalb()); //射正b
            map.put("foulnumb", str.getFoulnumb()); //犯规b
            map.put("cornerkicknumb", str.getCornerkicknumb());//角球b
            map.put("offsidenumb", str.getOffsidenumb());//越位b
            map.put("yellowcardnumb", str.getYellowcardnumb());//黄牌b
            map.put("savesb", str.getSavesb());//救球b
            map.put("playerb", str.getPlayerb());//客队名单b
            map.put("tbplayerb", str.getTbplayerb());//替补b
            list.add(map);
        }
        Map dataMap = new HashMap();
        dataMap.put("list", list);
        logger.info("ftBallSkill  足球实况(技术统计 首发名单和替补名单)---------End---------");
        return HttpResultUtil.successJson(dataMap);
    }
}
