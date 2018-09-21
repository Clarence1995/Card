package com.tecsun.card.entity.beandao.collect;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BasicPersonInfoDAO {
    /**
     * 身份证号
     */
    private String certNum;
    /**
     * 姓名
     */
    private String name;
    /**
     * 同步状态
     */
    private String synchroStatus;
    /**
     * 人员处理状态
     */
    private String dealStatus;
    /**
     * 校验错误信息
     */
    private String dealMsg;
    /**
     * 区划代码
     */
    private String regionalCode;
    /**
     * 是否为婴儿卡
     */
    private String babyCard;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * IDList,用于批量修改
     */
    private List<Long> idList;


}
