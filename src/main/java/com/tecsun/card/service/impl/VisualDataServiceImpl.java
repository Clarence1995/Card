package com.tecsun.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.common.clarencezeroutils.EchartUtils;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.vo.echarts.CollectDataBean;
import com.tecsun.card.entity.vo.echarts.MidTabelBean;
import com.tecsun.card.entity.vo.echarts.TableBean;
import com.tecsun.card.entity.vo.echarts.VDDoughuntVO;
import com.tecsun.card.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("visualDataService")
public class VisualDataServiceImpl implements VisualDataService {
    @Autowired
    private CollectService collectService;
    @Autowired
    private CardService cardService;
    @Autowired
    private MidService midService;
    @Autowired
    private RedisService redisService;

    /**
     * 数据库可视化表格
     * 1、同步、未同步卡管人数占比
     * 2、重复人员数量
     * 3、卡管新申领状态
     *
     * @return
     */
    @Override
    public Result getVisualDataFromCollectAndCard () {
        Result result = new Result();
        List<VDDoughuntVO> resultList = new ArrayList<>();
        // 1、同步、未同步卡管人数占比
        // String
        List<VisualDataDoughunDAO> basicPersonAnalyse = collectService.getVDBasicPersonAnalyse();
        VDDoughuntVO vdDoughuntVO = new VDDoughuntVO();
        // 1、获取title
        List<String> titleList = new ArrayList<>();
        List<CollectDataBean> collectDataBeanList = new ArrayList<>();
        vdDoughuntVO.setLegendData(titleList);
        for (Map.Entry<Integer, String> entry : Constants.COLLECT_SYNCHROSTATUS_DIC.entrySet()) {
            titleList.add(entry.getValue());
        }
        for (VisualDataDoughunDAO visualDataDoughunDAO : basicPersonAnalyse) {
            int value = visualDataDoughunDAO.getVisualData();
            String key = visualDataDoughunDAO.getKey();
            String realKey = "";
            if (Constants.COLLECT_SYNCHROSTATUS_DIC.containsKey(key)) {
                realKey = Constants.COLLECT_SYNCHROSTATUS_DIC.get(key);
            } else {
                realKey = "同步状态未知";
            }
            CollectDataBean collectDataBean = new CollectDataBean(visualDataDoughunDAO.getVisualData(), realKey);
            collectDataBeanList.add(collectDataBean);
        }
        vdDoughuntVO.setSeriesData(collectDataBeanList);
        result.success(vdDoughuntVO);
        return result;
    }

    @Override
    public Result getTableCollectBasic (String flush, String redisField) {
        Result result = new Result();
        Map<String, Object> resultMap = new HashMap<>();
        // 返回的结果集
        // tableTitle
        List<String> strings = new ArrayList<>();
        strings.add("同步名称_synchroName");
        strings.add("人数_synchroCount");
        strings.add("同步状态码_synchroStatus");
        strings.add("数据获取时间_flushTime");
        List<Map<String, String>> list = EchartUtils.elementUITitle(strings);

        resultMap.put("tableHeader", list);
        List<Object> resultList = new ArrayList<>();
        List<VisualDataDoughunDAO> basicPersonAnalyse = collectService.getVDBasicPersonAnalyse();
        int i = 0;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String currentTime = sdf.format(d);
        for (VisualDataDoughunDAO visualDataDoughunDAO : basicPersonAnalyse) {
            int value = visualDataDoughunDAO.getVisualData();
            String key = visualDataDoughunDAO.getKey();
            String realKey = "";
            if (Constants.COLLECT_SYNCHROSTATUS_DIC.containsKey(key)) {
                realKey = Constants.COLLECT_SYNCHROSTATUS_DIC.get(key);
            } else {
                realKey = "同步状态未知";
            }
            TableBean tableBean = new TableBean();
            tableBean.setId(i);
            tableBean.setSynchroCount(String.valueOf(value));
            tableBean.setSynchroName(realKey);
            tableBean.setSynchroStatus(String.valueOf(key));
            tableBean.setFlushTime(currentTime);
            resultList.add(tableBean);
            i++;
        }
        resultMap.put("tableData", resultList);

        // 添加到Redis
        String redisValue = JSONObject.toJSONString(resultMap);
        redisService.hset("CARD:HASH:ECHART", redisField, redisValue);
        result.success();
        result.setData(redisValue);
        return result;
    }

    /**
     * @return
     */
    @Override
    public Result getTableMidUserInfo (String flush, String redisField) {
        Map<String, Object> resultMap = new HashMap<>();
        Result result = new Result();
        // 1、组装表头
        List<String> strings = new ArrayList<>();
        strings.add("人员状态_userStatusName");
        strings.add("人数_userCount");
        strings.add("同步状态码_userStatusCode");
        strings.add("同步更新时间_flushTime");
        List<Map<String, String>> list = EchartUtils.elementUITitle(strings);
        resultMap.put("tableHeader", list);

        // 2、组装表数据
        List<VisualDataDoughunDAO> basicPersonAnalyse = midService.getVDCollectAC01();
        List<Object> list1 = new ArrayList<>();
        int i = 1;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String currentTime = sdf.format(d);
        for (VisualDataDoughunDAO visualDataDoughunDAO : basicPersonAnalyse) {
            MidTabelBean midTabelBean = new MidTabelBean();
            int count = visualDataDoughunDAO.getVisualData();
            String status = visualDataDoughunDAO.getKey();
            String userStatusName = "";
            if (Constants.COLLECT_SYNCHROSTATUS_DIC.containsKey(status)) {
                userStatusName = Constants.COLLECT_SYNCHROSTATUS_DIC.get(status);
            } else {
                userStatusName = "人员状态未知";
            }
            midTabelBean.setId(i);
            midTabelBean.setUserStatusCode(status);
            midTabelBean.setUserStatusName(userStatusName);
            midTabelBean.setUserCount(String.valueOf(count));
            midTabelBean.setFlushTime(currentTime);
            list1.add(midTabelBean);
        }
        resultMap.put("tableData", list1);
        String redisValue = JSONObject.toJSONString(resultMap);
        redisService.hset("CARD:HASH:ECHART", redisField, redisValue);
        result.success();
        result.setData(redisValue);
        return result;
    }

    /**
     * 卡管 Table
     *
     * @return
     */
    @Override
    public Result getTableCardUserInfo (String flush, String redisField) {
        Map<String, Object> resultMap = new HashMap<>();
        Result result = new Result();
        // 1、组装表头
        List<String> strings = new ArrayList<>();
        strings.add("人员状态_userStatusName");
        strings.add("人数_userCount");
        strings.add("同步状态码_userStatusCode");
        strings.add("同步更新时间_flushTime");
        List<Map<String, String>> list = EchartUtils.elementUITitle(strings);
        resultMap.put("tableHeader", list);

        // 2、组装表数据
        List<VisualDataDoughunDAO> basicPersonAnalyse = cardService.getVDCollectAC01();
        List<Object> list1 = new ArrayList<>();
        int i = 1;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String currentTime = sdf.format(d);
        for (VisualDataDoughunDAO visualDataDoughunDAO : basicPersonAnalyse) {
            MidTabelBean midTabelBean = new MidTabelBean();
            int count = visualDataDoughunDAO.getVisualData();
            String status = visualDataDoughunDAO.getKey();
            String userStatusName = "";
            if (Constants.COLLECT_SYNCHROSTATUS_DIC.containsKey(status)) {
                userStatusName = Constants.COLLECT_SYNCHROSTATUS_DIC.get(status);
            } else {
                userStatusName = "人员状态未知";
            }
            midTabelBean.setId(i);
            midTabelBean.setUserStatusCode(status);
            midTabelBean.setUserStatusName(userStatusName);
            midTabelBean.setUserCount(String.valueOf(count));
            midTabelBean.setFlushTime(currentTime);
            list1.add(midTabelBean);
        }
        resultMap.put("tableData", list1);
        String redisValue = JSONObject.toJSONString(resultMap);
        redisService.hset("CARD:HASH:ECHART", redisField, redisValue);
        result.success();
        result.setData(redisValue);
        return result;
    }
}
