package com.tecsun.card.entity;

import com.tecsun.card.common.excel.Excel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicInfoBean {
    private String idKey;
    /**
     * 身份证号
     */
    @Excel(name = "社会保障号码")
    private String AAC147;
    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String AAC003;
    /**
     * 藏名
     */
    private String AAC003A;
    /**
     * 性别
     */
    private String AAC004;
    /**
     * 民族
     */
    private String AAC005;
    /**
     * 出生日期
     */
    private String AAC006;
    /**
     * 手机号码
     */
    @Excel(name = "联系电话")
    private String AAC067;

    private String AAC058;

    private long cardId;
}
