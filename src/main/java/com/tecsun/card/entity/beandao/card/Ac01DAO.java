package com.tecsun.card.entity.beandao.card;

import com.tecsun.card.entity.BasicInfoBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ac01DAO extends BasicInfoBean {
    /**
     * idKey
     */
    private Long id;
    /**
     * 人员状态
     */
    private String userStatus;

    /**
     * 申领状态
     */
    private String applyStatus;
    /**
     * 申领类型
     */
    private String applyType;
    /**
     * 证件类型
     */
    private String AAC058;
    /**
     * 身份证号
     */
    private String AAC147;
    /**
     * 姓名
     */
    private String AAC003;
    /**
     * 银行账号
     */
    private String AAE053;
    /**
     * 行政规划
     */
    private String AAC301;

    /**
     * 社会保障卡卡号 9位
     */
    private String AAZ500;

    /**
     * 单位编号
     */
    private String AAB001;
    /**
     * 单位名称
     */
    private String AAB004;
}
