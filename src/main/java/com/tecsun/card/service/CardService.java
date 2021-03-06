package com.tecsun.card.service;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.AZ01PO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.SynchroExcelVO;
import com.tecsun.card.exception.MyException;

import java.util.List;

public interface CardService {


    //  ~ GET ---------------------------------------------

    /**
     * @return
     * @Description 获取用户所有信息
     * @params
     * @author 0214
     * @createTime 2018-09-14 16:51
     * @updateTime
     */
    Ac01PO getAC01DetailByIdCardAndName(String idCard, String name);

    /**
     * 根据用户名和身份证号获取用户(只返回身份证号,但是不去重,用于判断重复或是否存在此此用户)
     *
     * @param idCard
     * @param name
     * @return
     */
    boolean userExistJudgeByIdCardAndName(String idCard, String name);

    /**
     * @return java.lang.String
     * @Description AC01个人编号生成
     * @param:
     * @author 0214
     * @createTime 2018-09-20 10:52
     * @updateTime
     */
    String generateAC01IUserNumber() throws Exception;


    /**
     * @return long
     * @Description 从数据库获取序列
     * @param:
     * @author 0214
     * @createTime 2018-09-20 10:52
     * @updateTime
     */
    long getUserSeq();

    /**
     * @return com.tecsun.card.entity.po.AZ01PO
     * @Description 根据用户获取的CARD_ID 获取对应的Card信息
     * @param: cardId
     * @author 0214
     * @createTime 2018-09-20 10:53
     * @updateTime
     */
    AZ01PO getCardByUserId(Long cardId);

    /**
     * @return java.util.List<com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO>
     * @Description 可视化数据
     * @param:
     * @author 0214
     * @createTime 2018-09-20 10:54
     * @updateTime
     */
    List<VisualDataDoughunDAO> getVDCollectAC01();

    /**
     * @return java.util.List<com.tecsun.card.entity.beandao.card.Ac01DAO>
     * @Description 获取卡管库所有人员ID和姓名
     * @param:
     * @author 0214
     * @createTime 2018-09-25 10:44
     * @updateTime
     */
    List<Ac01DAO> listAllUserIdCard();


    /**
     * @return com.tecsun.card.entity.po.Ac01PO
     * @Description 通过区域ID、人员状态获取人员详情
     * @param: regionalCode
     * @param: userStatus
     * @author 0214
     * @createTime 2018-09-26 16:02
     * @updateTime
     */
    List<Ac01PO> getAC01DetailByRegeionalCodeAndStatus(String regionalCode, String userStatus);

    /**
     * 获取指定数据的序列
     * @param count
     * @return
     */
    List<Long> getAC01SequenceBatch(int count);

    // ~ UPDATE ---------------------------------------------

    // ~ DELETE ---------------------------------------------

    /**
     * 根据身份证、姓名删除AC01表中的人员数据
     * @param idCard
     * @param userName
     * @return
     */
    boolean deleteAC01ByIdCardAndName(String idCard, String userName) throws Exception;

    // ~ INSERT ---------------------------------------------

    /**
     * @return boolean
     * @Description AC01表插入
     * @param: ac01PO
     * @author 0214
     * @createTime 2018-09-20 10:56
     * @updateTime
     */
    boolean insertAC01(Ac01PO ac01PO) throws MyException;


    /**
     * @return boolean
     * @Description BUS_APPLY 申领表插入
     * @param: busApplyPO
     * @author 0214
     * @createTime 2018-09-20 11:00
     * @updateTime
     */
    boolean insertBusApply(BusApplyPO busApplyPO);


    /**
     * @return boolean
     * @Description BUS_APPLY 和 AC01 同时插入 == 新申领操作
     * @param: ac01bean
     * @param: basicPersonInfo
     * @author 0214
     * @createTime 2018-09-20 11:01
     * @updateTime
     */
    boolean insertCardAC01AndBusApplyFromCollect(Ac01PO ac01bean, BasicPersonInfo basicPersonInfo) throws Exception;


    /**
     * 批量插入数据库操作
     * @param ac01Bean
     * @param busApplyBean
     * @param basicPersonInfo
     * @return
     * @throws Exception
     */
    boolean insertAC01AndBusApplyAndUpdateCollect(List<Ac01PO> ac01Bean, List<BusApplyPO> busApplyBean, List<BasicPersonInfo> basicPersonInfo) throws Exception;


    // ~ ELSE

    /**
     * @return void
     * @Description 组装AC01
     * @param: ac01PO
     * @param: bean
     * @author 0214
     * @createTime 2018-09-20 13:07
     * @updateTime
     */
    void assembleAC01(Ac01PO ac01PO, BasicPersonInfo bean, boolean ePriority) throws Exception;


    int updateAC01Status(Ac01DAO acstatusBean);


    Result synchroFromExcel(SynchroExcelVO synchroExcelVO) throws Exception;

    /**
     * 批量插入AC01表
     * @param userList
     * @return
     */
    int insertAc01Batch(List<Ac01PO> userList);

    /**
     * 批量插入申领表
     * @param busApplyList
     * @return
     */
    int insertBusApplyBatch(List<BusApplyPO> busApplyList);

}
