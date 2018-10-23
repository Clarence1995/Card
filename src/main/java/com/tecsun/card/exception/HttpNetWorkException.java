package com.tecsun.card.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 0214
 * @createTime 2018/10/12
 * @description
 */
@Getter
@Setter
public class HttpNetWorkException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;


    public HttpNetWorkException(String msg) {
        super(msg);
    }

    public HttpNetWorkException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public HttpNetWorkException(String errorCode, String msg, Throwable cause ) {
        super(msg, cause);
        this.errorCode = errorCode;

    }
}
