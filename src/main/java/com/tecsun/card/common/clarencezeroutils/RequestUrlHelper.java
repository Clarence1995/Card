package com.tecsun.card.common.clarencezeroutils;

import com.tecsun.card.entity.bo.RequestUrlInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RequestUrlHelper {

    private static final RequestUrlHelper helper = new RequestUrlHelper();

    protected RequestUrlHelper() {

    }

    public static RequestUrlHelper getInstance() {
        return helper;
    }

    /**
     * 将RequestMapping注解信息，组装成RequestUrlInfo类中。此类方法共有三种重载方式，分别为Annotation、提供basePath、提供classAnnotation注解三种方式。
     * @param annotation
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private List<RequestUrlInfo> getRequestUrlInfos(Annotation annotation)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<RequestUrlInfo> infos = new ArrayList<RequestUrlInfo>();

        if(annotation == null) {
            return infos;
        }

        String name = (String) AnnotationHelper.getInstance().getAnnotationInfo(annotation, "name");
        List<String> requestUrls = Arrays.asList((String[]) AnnotationHelper.getInstance().getAnnotationInfo(annotation, "value"));
        List<RequestMethod> requestMethods = Arrays.asList((RequestMethod[]) AnnotationHelper.getInstance().getAnnotationInfo(annotation, "method"));

        if(requestMethods.isEmpty()) {
            for(String url : requestUrls) {
                RequestUrlInfo info = new RequestUrlInfo(name);

                info.setValue(url);
                info.setRequestMethod(null);

                infos.add(info);
            }
        } else {
            for(String url : requestUrls) {
                for(RequestMethod method : requestMethods) {
                    RequestUrlInfo info = new RequestUrlInfo(name);

                    info.setValue(url);
                    info.setRequestMethod(method);

                    infos.add(info);
                }
            }
        }

        return infos;
    }

    /**
     * 上一个方法的重载方法，主要是将类上面的注解中的路径添加到全部的请求信息中。
     * @param basePath
     * @param annotation
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private List<RequestUrlInfo> getRequestUrlInfos(String basePath , Annotation annotation) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<RequestUrlInfo> infos = getRequestUrlInfos(annotation);

        if(!StringUtils.hasText(basePath)) {
            return infos;
        }

        for(RequestUrlInfo info : infos) {
            info.setValue(basePath.concat("/" + info.getValue()));
        }

        return infos;
    }

    /**
     * 上一个方法的重载方法，这次，没有提供basePath，而是提供一个Annotation，并从这个注解上面获取得到类上面注解的请求路径。
     * @param classAnnotation
     * @param methodAnnotation
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private List<RequestUrlInfo> getRequestUrlInfos(Annotation classAnnotation , Annotation methodAnnotation) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        List<RequestUrlInfo> infos = new ArrayList<RequestUrlInfo>();

        if(classAnnotation != null) {
            String[] paths = (String[]) AnnotationHelper.getInstance().getAnnotationInfo(classAnnotation, "value");

            for(String path : paths) {
                infos.addAll(getRequestUrlInfos(path, methodAnnotation));
            }
        } else {
            infos.addAll(getRequestUrlInfos(methodAnnotation));
        }

        return infos;
    }

    /**
     * 获取全部的请求，将其包装成RequestUrlInfo实体类，并将其通过请求路径排序后，进行返回。
     * @param scannerClass
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public List<RequestUrlInfo> getAllRequestUrlInfos(Class<?> scannerClass) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Annotation classAnnotation = AnnotationHelper.getInstance().getClassAnnotation(scannerClass, RequestMapping.class);
        List<Annotation> methodAnnotations = AnnotationHelper.getInstance().getMethodAnnotation(scannerClass, RequestMapping.class);
        List<RequestUrlInfo> infos = new ArrayList<RequestUrlInfo>();

        for(Annotation methodAnnotation : methodAnnotations) {
            infos.addAll(getRequestUrlInfos(classAnnotation, methodAnnotation));
        }

        Collections.reverse(infos);

        return infos;
    }
}
