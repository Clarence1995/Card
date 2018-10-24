package com.tecsun.card.service;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.vo.GongAnInfoVO;
import com.tecsun.card.exception.HttpNetWorkException;

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
     * @Description 单个人员数据同步同步
     * @return com.tecsun.card.entity.Result
     * @param: idCard                          身份证号码
     * @param: imgFilePath                     数据中心处理过的照片路径
     * @param: eCopyImgFromHadDeal             是否需要复制数据中心处理过的照片到本地
     * @param: egetImgFromDatabase             是否从本地数据库获取公安照片
     * @param: eValidateUserInfo               是否校验人员基础信息
     * @param: eGetCollectDataFromFourty       是否需要从40采集库获取人员基本信息
     * @param: eCompareWithGongAnDatabase      是否需要和公安库进行比对
     * @param: eMarkPriority                   是否需要将人员状态标记为优先
     * @param: eDeleteAC01User                 是否需要删除AC01表(用于人员异常信息再同步)
     * @author 0214
     * @createTime 2018-10-11 11:20
     * @updateTime
     */
    public Result handleCollectSynchro(String idCard,
                                       Boolean egetImgFromDatabase,
                                       Boolean eValidateUserInfo,
                                       Boolean eGetCollectDataFromFourty,
                                       Boolean eCompareWithGongAnDatabase,
                                       Boolean eDeleteAC01User,
                                       Boolean eCopyImgFromHadDeal,
                                       Boolean eMarkPriority,
                                       String imgFilePath
    );


    /**
     * @return com.tecsun.card.entity.Result
     * @Description 处理采集库人员数据重复 人员重复数据同步状态置为9: 表示可删除,卡管已同步置为1,未同步置为0
     * @param: logFilePath
     * @param: threadCount
     * @author 0214
     * @createTime 2018-09-21 15:26
     * @updateTime
     */
    Boolean handleCollectDateRepeat(String idCard, String logFilePath) throws IOException;


    /**
     * 根据IdCard从数据库获取照片
     *
     * @param idCard  idCard
     * @param copyImg 是否复制照片到指定文件夹
     * @param args    可变参数 指定文件夹路径
     * @return
     * @throws IOException
     */
    Boolean getImgFromDatabaseByIdCard(String idCard, boolean copyImg, String... args) throws IOException;


    /**
     * 根据IdCard判断并复制TSB照片
     *
     * @param idCard
     * @param copyImg
     * @param args
     * @return
     * @throws IOException
     */
    Boolean getImgFromTSBByIdCard(String idCard, boolean copyImg, String... args) throws IOException;

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
     *
     * @param logFilePath
     */
    Result handleDongRuanSynchro(List<BasicPersonInfoDAO> collectList, List<Ac01DAO> cardList, String logFilePath);

    /**
     * 审核采集人员数据
     * @param userInfo
     * @return
     */
    Boolean checkCollectUserInfo(BasicPersonInfo userInfo);

    /**
     * 依据身份证号码调用公安接口获取用户信息
     * @param idCard
     * @return
     */
    GongAnInfoVO getUserInfoFromGongAnByIdCard(String idCard) throws Exception;

    /**
     * 和公安数据库进行比对
     * @param userInfo
     * @return
     */
    Result compareWithGongAnUserInfo(BasicPersonInfo userInfo) throws Exception;

    List<GongAnInfoVO> getUserInfoFromGongAnByIdCardList(List<GongAnInfoVO> userInfoList) throws Exception;

}
