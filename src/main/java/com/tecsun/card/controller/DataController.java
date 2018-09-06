package com.tecsun.card.controller;

import com.tecsun.card.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 0214
 * @createTime 2018/9/6
 * @description
 */
@RequestMapping("data")
@RestController
public class DataController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test() {
        Result result = new Result();
        result.setStateCode(200);
        result.setMsg("测试成功");
        return result;
    }
}
