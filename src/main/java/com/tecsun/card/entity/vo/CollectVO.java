package com.tecsun.card.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *@Description
 *@author  0214
 *@createTime  2018/9/4
 *@updateTime
 */
@Getter
@Setter
@ToString
public class CollectVO {
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
     * 日志路径
     */
    private String logPath;
    /**
     * 图片路径
     */
    private String imgPath;
    /**
     * 线程数量
     */
    private Integer threadCount;

    /**
     * 图片是否从数据库获取
     */
    private Boolean imgFromData;
    /**
     * 区域编码
     */
    private String regionalCode;
    /**
     * 处理状态
     */
    private String dealStaus;
    /**
     * 处理信息
     */
    private String dealMsg;

    /**
     * 是否为婴儿卡
     */
    private String badyCard;

    /**
     * 是否已完成东软单位信息同步 0: 未同步 1、已同步 2:此人在东软视图中不存在信息
     */
    private Integer dongRuanSynchroStatus;

    /**
     * 单位名称
     */
    private String departmentName;
    /**
     * 单位编号
     */
    private String departmentNo;
}
