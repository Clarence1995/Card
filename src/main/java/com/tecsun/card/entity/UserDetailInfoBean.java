package com.tecsun.card.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailInfoBean extends BasicInfoBean {
    /**
     * 证件类型
     */
    private String AAC058;
    /**
     * 户口所在地
     */
    private String AAC010;
    /**
     * 国家、地址代码
     */
    private String AAC161;
    /**
     * 常住地行政区划代码
     */
    private String AAC301;
    /**
     * 区县
     */
    private String aac301a;
    /**
     * 街道
     */
    private String aac301b;

}
