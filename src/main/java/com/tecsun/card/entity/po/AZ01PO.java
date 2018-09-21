package com.tecsun.card.entity.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 0214
 * @createTime 2018/9/14
 * @description
 */
@Getter
@Setter
@ToString
public class AZ01PO {
    private Long id;
    private String status;
    /**
     * 卡应用状态
     */
    private int AAZ502;

}
