package com.tecsun.card.service;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;

import java.io.IOException;
import java.util.List;

/**
 * @author 0214
 * @createTime 2018/9/7
 * @description 数据处理Service
 */
public interface DataHandleService {
    /**
     * 人员数据详情
     *
     * @param filePath TXT文件路径
     * @param logPath  日志文件路径
     * @param copy     是否复制
     * @return Result   结果集
     */
    Result handleUserInfo(String filePath, String logPath, boolean copy);


    /**
     * @return com.tecsun.card.entity.Result
     * @Description 处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0
     * @param: logFilePath
     * @param: threadCount
     * @author 0214
     * @createTime 2018-09-21 15:26
     * @updateTime
     */
    Result handleCollectDateRepeat(String logFilePath, Integer threadCount) throws IOException;


    /**
     * 根据IdCard从数据库获取照片
     *
     * @param idCard  idCard
     * @param copyImg 是否复制照片到指定文件夹
     * @param args    可变参数 指定文件夹路径
     * @return
     * @throws IOException
     */
    boolean getImgFromDatabaseByIdCard(String idCard, boolean copyImg, String... args) throws IOException;


    /**
     * 根据IdCard判断并复制TSB照片
     *
     * @param idCard
     * @param copyImg
     * @param args
     * @return
     * @throws IOException
     */
    boolean getImgFromTSBByIdCard(String idCard, boolean copyImg, String... args) throws IOException;

    /**
     * 依据东软数据库更新卡管库、采集库单位名称和单位编号
     *
     * @param databaseName
     * @param threadCount
     * @return
     */
    Result handleCollectDepartmentNo(String databaseName, String logFilePath, Integer threadCount);

    /**
     * 依据东软数据库更新卡管库、采集库单位名称和单位编号
     * @param logFilePath
     */
    Result handleDongRuanSynchro(List<BasicPersonInfoDAO> collectList, List<Ac01DAO> cardList,String logFilePath);

    }
