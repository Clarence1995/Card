package com.tecsun.card.controller;

import com.tecsun.card.common.excel.Excel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchBean {

    @Excel(name = "个人编码")
    private String AAC001;

    @Excel(name = "社会保障号码")
    private String AAC147;

    @Excel(name = "姓名")
    private String AAC003;

    @Excel(name = "姓名扩展")
    private String AAC003A;

    @Excel(name = "性别码")
    private String AAC004;

    @Excel(name = "出生日期")
    private String AAC006;

    @Excel(name = "联系电话")
    private String AAC067;

    @Excel(name = "卡识别码")
    private String AAZ501;

    @Excel(name = "银行卡号")
    private String AAE053;

    @Excel(name = "卡号")
    private String AAZ500;

    private String AAC058;

    private String batchId;

    private String batchNo;

}
