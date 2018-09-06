package com.tecsun.card.entity.vo;

import com.tecsun.card.common.excel.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 0214
 * @createTime 2018/9/5
 * @description
 */
@Getter
@Setter
public class ExcelTmpVO {
    /**
     *
     */
    @Excel(name = "身份证号码")
    private String idCard;
    @Excel(name = "姓名")
    private String name;
}
