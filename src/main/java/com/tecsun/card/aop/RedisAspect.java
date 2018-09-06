package com.tecsun.card.aop;

import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.entity.Result;
import com.tecsun.card.service.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Redis控制AOP拦截
 * @author 0214
 */
@Component
@Aspect
public class RedisAspect {
    private static final Logger logger = LoggerFactory.getLogger(RedisAspect.class);
    @Autowired
    private RedisService redisService;
    // @Pointcut("execution(* com.tecsun.card.controller.rediscontroller.DashboardController..*.*())")
    // public void myPointCut(){};

    @Around("execution(* com.tecsun.card.controller.rediscontroller..*.*(..))")
    public Object around (ProceedingJoinPoint joinPoint) throws Throwable {
        // 判断是否是flush
        Object[] args = joinPoint.getArgs();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String url = request.getRequestURL().toString();
        String redisField = url.substring(url.lastIndexOf("/") + 1, url.length());
        // 在这里解析URL 1、包含System的,则HASH key设置为 SYSTEM, 如果包含echart则设置为ECHART
        StringBuilder str = new StringBuilder();
        str.append("CARD:HASH:");
        if (url.indexOf("system") > -1) {
            str.append("SYSTEM");
        } else if (url.indexOf("echart") >-1) {
            str.append("ECHART");
        }

        // 获取是否有刷新
        Result result = new Result();
        boolean flush = Boolean.parseBoolean(request.getParameter("flush"));
        if (flush) {
            // 如果需要刷新,则直接返回
            return joinPoint.proceed(args);
        } else {
            // 返回 Redis数据库
            String redisResultStr = redisService.hget(str.toString(), redisField);
            // 如果为空,则继续
            if (!ObjectUtils.notEmpty(redisResultStr)) {
                return joinPoint.proceed(args);
            } else {
                result.success();
                result.setData(redisResultStr);
                return result;
            }
        }
    }
}
