package com.tecsun.card.service;

import com.tecsun.card.entity.Result;

import java.io.IOException;

/**
 * @author 0214
 * @createTime 2018/9/7
 * @description 数据处理Service
 */
public interface DataHandleService {
    /**
     * 人员数据详情
     * @param filePath  TXT文件路径
     * @param logPath   日志文件路径
     * @param copy      是否复制
     * @return Result   结果集
     */
    Result handleUserInfo(String filePath, String logPath, boolean copy);


    /**
     * @Description  处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0
     * @param: logFilePath
    * @param: threadCount
     * @return  com.tecsun.card.entity.Result
     * @author  0214
     * @createTime 2018-09-21 15:26
     * @updateTime
     */
    Result handleCollectDateRepeat(String logFilePath, Integer threadCount);


    /**
     * 根据IdCard从数据库获取照片
     * @param idCard    idCard
     * @param copyImg   是否复制照片到指定文件夹
     * @param args      可变参数 指定文件夹路径
     * @return
     * @throws IOException
     */
    boolean getImgFromDatabaseByIdCard(String idCard, boolean copyImg, String... args) throws IOException;


    /**
     * 根据IdCard判断并复制TSB照片
     * @param idCard
     * @param copyImg
     * @param args
     * @return
     * @throws IOException
     */
    boolean getImgFromTSBByIdCard(String idCard, boolean copyImg, String... args) throws IOException;

}
