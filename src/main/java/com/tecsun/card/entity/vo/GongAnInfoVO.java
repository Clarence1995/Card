package com.tecsun.card.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 0214
 * @createTime 2018/10/11
 * @description
 */
@Getter
@Setter
public class GongAnInfoVO {
    /**
     * 姓名
     */
    private String XM;
    /**
     * 身份证号
     */
    private String SFZH;
    /**
     * 性别
     */
    private Integer XB;
    /**
     * 民族
     */
    private Integer MZ;
    /**
     * 出生日期
     */
    private Long CSRQ;
    /**
     * 户籍所在地
     */
    private String HKSZD;
}
