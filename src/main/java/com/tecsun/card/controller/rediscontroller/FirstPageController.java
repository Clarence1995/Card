package com.tecsun.card.controller.rediscontroller;

import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.entity.Result;
import com.tecsun.card.service.VisualDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;



/**
 * 该控制类用于Card首页可视化数据
 * @author 0214 
 * @date 2018/8/21 16:01
 * @param
 * @return
 */
@Controller
@RequestMapping(value = "echart")
public class FirstPageController {
    @Autowired
    private VisualDataService visualDataService;

    /**
     * 采集库基础人员信息统计接口
     * @author 0214
     * @date 2018/8/21 16:02
     * @param
     * @return
     */
    @RequestMapping(value = "tableCollect", method = RequestMethod.GET)
    @ResponseBody
    public Result getTableCollectBasic (HttpServletRequest request) {
        String flush = request.getParameter("flush");
        if (!ObjectUtils.notEmpty(flush)) {
            flush = "false";
        }
        return visualDataService.getTableCollectBasic(flush, "tableCollect");
    }

    /**
     * 中间库基础人员信息统计接口
     * @author 0214
     * @date 2018/8/21 16:01
     * @param
     * @return
     */
    @RequestMapping(value = "tableMid", method = RequestMethod.GET)
    @ResponseBody
    public Result getTableMidUserInfo (HttpServletRequest request) {
        String flush = request.getParameter("flush");
        if (!ObjectUtils.notEmpty(flush)) {
            flush = "false";
        }
        return visualDataService.getTableMidUserInfo(flush, "tableMid");
    }

     /**
      * 卡管库基础人员信息统计接口
      * @author 0214
      * @date 2018/8/21 16:03
      * @param 
      * @return 
      */
    @RequestMapping(value = "tableCard", method = RequestMethod.GET)
    @ResponseBody
    public Result getTableCardInfo (HttpServletRequest request) {
        String flush = request.getParameter("flush");
        if (!ObjectUtils.notEmpty(flush)) {
            flush = "false";
        }
        return visualDataService.getTableCardUserInfo(flush, "tableCard");
    }

    /**
     * EChart图 采集表甜甜圈图
     *
     * @return
     */
    @RequestMapping(value = "collectDoughnut", method = RequestMethod.GET)
    @ResponseBody
    public Result doughnutChart () {
        return visualDataService.getVisualDataFromCollectAndCard();
    }
}

















