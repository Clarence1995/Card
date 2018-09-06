package com.tecsun.card.common.datasource;

import java.lang.reflect.Proxy;

import org.apache.commons.lang.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 拦截切换数据源
 * @author tan
 *
 */
@Aspect
@Component
public class DynamicDataSourceIntercept {

    
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceIntercept.class);

    @Pointcut("execution(* com.tecsun.sisp.iface.modules.service.impl..*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
    	 System.out.println("================数据源切换开始处理=================");
         Class<?> clazz = joinPoint.getTarget().getClass();

         String className = clazz.getName();
         if(ClassUtils.isAssignable(clazz,Proxy.class)){
             className = joinPoint.getSignature().getDeclaringTypeName();
         }
         String methodName = joinPoint.getSignature().getName();

         System.out.println("类名："+className+" , 方法名："+methodName);
         if (className.contains("zdcx")) {
             CustomerContextHolder.setCustomerType(CustomerContextHolder.ZDCX_DATASOURCE);
         } else if (className.contains("medic")) {
             CustomerContextHolder.setCustomerType(CustomerContextHolder.MEDIC_DATASOURCE);
         } else if (methodName.contains("zdcx")) {
             CustomerContextHolder.setCustomerType(CustomerContextHolder.ZDCX_DATASOURCE);
         } else if (methodName.contains("medic")) {
             CustomerContextHolder.setCustomerType(CustomerContextHolder.MEDIC_DATASOURCE);
         } else {
             CustomerContextHolder.removeCustomerType();
         }
    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        logger.error("异常类=======:" + joinPoint.getTarget().getClass() + "==" + e.getMessage());
        System.out.println("异常类=======:" + joinPoint.getTarget().getClass() + "==" + e.getMessage());
    }
}
