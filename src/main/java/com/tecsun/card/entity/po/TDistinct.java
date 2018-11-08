package com.tecsun.card.entity.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TDistinct {
    /**
     * 区域ID
     */
    private Float distinctId;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 父级ID
     */
    private Float parentId;

    /**
     *
     */
    private Date createTime;

    private Date updateTime;

}