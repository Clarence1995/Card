package com.tecsun.card.common.excel;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个Excel只适用于简单的行与列,并不能设置复杂的Excel表格式,不包括合并单元格
 * 功能: 自定义列名、列宽、
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Excel {
    // 列名
    String name () default "";

    // 宽度
    int width () default 20;

    // 忽略该字段
    boolean skip () default false;
}
