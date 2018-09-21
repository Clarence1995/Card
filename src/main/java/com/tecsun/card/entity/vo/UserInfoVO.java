package com.tecsun.card.entity.vo;

import com.tecsun.card.common.excel.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 0214
 * @Description 用于 handleUserInfo 记录人员信息
 * @createTime 2018/9/14
 * @description
 */
@Getter
@Setter
@ToString
public class UserInfoVO {
    @Excel(name = "姓名")
    private String name;

    @Excel(name = "身份证号码", width = 25)
    private String idCard;

    @Excel(name = "基础信息校验", width = 25)
    private Boolean userInfoValid;

    @Excel(name = "基础信息详情")
    private String dealMsg;

    @Excel(name = "采集库存在校验")
    private Boolean collectHas;

    @Excel(name = "采集库重复校验")
    private Boolean userCollectRepeat;

    @Excel(name = "公安照片存在校验")
    private Boolean databaseImgHas;

    @Excel(name = "TSB照片存在校验")
    private Boolean tsbImgHas;



    @Excel(name = "卡管库存在校验")
    private Boolean cardHas;

    @Excel(name = "人员状态")
    private String  userStatus;

    @Excel(name = "卡管库重复校验")
    private String userCardRepeat;

    @Excel(name = "社保卡存在校验")
    private String eCard;

    @Excel(name = "卡ID")
    private Long    cardId;

    @Excel(name = "区划编码")
    private String  regionalCode;

    @Excel(name = "卡状态")
    private String  cardStatus;

    @Excel(name = "卡应用状态")
    private int     cardApplyStatus;
}
