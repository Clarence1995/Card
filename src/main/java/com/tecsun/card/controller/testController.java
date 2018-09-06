package com.tecsun.card.controller;

import com.tecsun.card.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@Api(description = "收货地址")
public class testController {
    private static final Logger logger = LoggerFactory.getLogger(testController.class);
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ApiOperation(value = "获得所有收货地址")
    @ResponseBody
    public Result test() {
        logger.info("请求成功：状态码{}", 200);
        Result result = new Result();
        result.setData(200);
        result.setMsg("请求成功");
        return result;
    }
}
