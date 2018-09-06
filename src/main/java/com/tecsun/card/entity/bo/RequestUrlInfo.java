package com.tecsun.card.entity.bo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

@Getter
@Setter
public class RequestUrlInfo implements Comparable<RequestUrlInfo>{

    /**
     * mapping的名称
     */
    private String name;

    /**
     * mapping的请求路径
     */
    private String value;
    /**
     * 响应请求的方法
     */
    private RequestMethod requestMethod;
    public RequestUrlInfo() {
        
    }

    public RequestUrlInfo(String name) {
        super();
        this.name = name;
    }



    /**
     * 根据请求路径对请求信息进行区分。
     */
    @Override
    public int compareTo(RequestUrlInfo o) {
        return this.value.compareTo(o.getValue());
    }
    
}