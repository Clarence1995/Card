package com.tecsun.card.controller;

import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.vo.UserVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "user")
public class UserController {
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Result userInfo(String username) {
        Result result = new Result();
        //roles": [
        //             "admin"
        //         ],
        //         "name": "admin",
        //         "avatar": "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", "admin");
        resultMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        ArrayList roles = new ArrayList<>();
        roles.add("admin");
        resultMap.put("roles",roles);
        result.setData(resultMap);
        result.success("获取用户信息成功");
        return result;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result userLogin(@RequestBody UserVO userVO) {
        Result result = new Result();
        String username = userVO.getUsername();
        String password = userVO.getPassword();
        if (!ObjectUtils.notEmpty(password)) {
            result.fail("密码不能为空");
            return result;
        } else if (!ObjectUtils.notEmpty(username)) {
            result.fail("用户名不能为空");
            return result;
        }
        if (!"admin".equals(username)) {
            result.fail("用户名错误");
            return result;
        } else if (!"123456".equals(password)) {
            result.fail("密码错误");
            return result;
        }
        String token = "admin";
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", "admin");
        result.setData(tokenMap);
        result.success("登录成功");
        return result;
    }
}
