package com.tecsun.card.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 0214
 * @createTime 2018/9/5
 * @description
 */
@Getter
@Setter
@ToString
public class SynchroExcelVO {
    /**
     * 是否开启Excel同步
     */
    private boolean excelSynchro;

    /**
     * Excel文件路径
     */
    private String excelFilePath;

    /**
     * 如果不开启Excel,则需要输入采集人员同步状态
     */
    private int collectUserStatus;

    /**
     * 日志记录路径
     */
    private String logFilePath;

    /**
     * 是否查找数据库公安图片
     */
    private boolean gonaAnSearch;

    /**
     * 德生宝照片文件夹
     */
    private String TSBFilePath;

    /**
     * 线程数量
     */
    private int threadNum;
}
