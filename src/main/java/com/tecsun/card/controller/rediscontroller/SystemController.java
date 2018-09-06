package com.tecsun.card.controller.rediscontroller;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.vo.RedisDictVO;
import com.tecsun.card.entity.vo.RequestMethodVO;
import com.tecsun.card.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("system")
public class SystemController {
    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "listDict", method = RequestMethod.GET)
    public Result listDict() {
        return systemService.initRedis();
    }

    @RequestMapping(value = "editDict", method = RequestMethod.PUT)
    public Result editDict(@RequestBody RedisDictVO redisDictVO) {
        return systemService.updateRedisDict(redisDictVO);
    }

    @RequestMapping(value = "url", method = RequestMethod.GET)
    public Result getControllerURL(HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // 存储所有url集合
        List<RequestMethodVO> uList = new ArrayList<>();
        // 获取上下文对
        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        // 通过上下文对象获取RequestMappingHandlerMapping实例对象
        RequestMappingHandlerMapping bean = wac.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            RequestMethodVO requestMethodVO = new RequestMethodVO();
            PatternsRequestCondition prc = rmi.getPatternsCondition();
            Set<String> patterns = prc.getPatterns();
            RequestMethodsRequestCondition method = rmi.getMethodsCondition();
            requestMethodVO.setMethod(method.toString());
            requestMethodVO.setURL(patterns.toString());
            uList.add(requestMethodVO);
        }
        Result result = new Result();
        result.setStateCode(200);
        result.setMsg("获取成功");
        result.setData(uList);
        return result;
    }

    @RequestMapping(value = "databaseDetail/{userName}", method = RequestMethod.GET)
    public Result getDatabaseDetail(@PathVariable String userName) {
        return systemService.getDatabaseDetail(userName);
    }

}
