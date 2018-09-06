package com.tecsun.card.controller;

import com.tecsun.card.entity.Result;
import com.tecsun.card.service.impl.DealBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("batch")
public class DealBatchController {
    private static final Logger logger = LoggerFactory.getLogger(DealBatchController.class);
    @Autowired
    private DealBatchService dealBatchService;

    @RequestMapping(value = "deal", method = RequestMethod.GET)
    public Result dealBatch (HttpServletRequest req) throws Exception {
        // 1、读取Excel
        // 2、判断数据库人员是否存在
        // 3、人员注销操作
        // 4、新增一条补换卡申领记录

        String filePath = req.getParameter("filePath");
        if (filePath == null || filePath.equals("")) {
            return new Result(0, "filePath路径为空");
        } else {
            return dealBatchService.dealBatch(filePath);
        }
    }


    /**
     * 处理补补换申领操作
     * @param req
     * @return
     */
    @RequestMapping(value = "BHKDeal", method = RequestMethod.GET)
    public Result handleBHK(HttpServletRequest req) throws Exception {
        String filePath = req.getParameter("filePath");
        if (filePath == null || filePath.equals("")) {
            return new Result(0, "filePath路径为空");
        } else {
            return dealBatchService.dealBHSL(filePath);
        }
    }

}
