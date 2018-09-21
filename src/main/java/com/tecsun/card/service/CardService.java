package com.tecsun.card.service;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.AZ01PO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
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


    // ~ UPDATE ---------------------------------------------

    // ~ DELETE ---------------------------------------------

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
     * @param: basicPersonInfoPO
     * @author 0214
     * @createTime 2018-09-20 11:01
     * @updateTime
     */
    boolean insertCardAC01AndBusApplyFromCollect(Ac01PO ac01bean, BasicPersonInfoPO basicPersonInfoPO);


    // ~ ELSE

    /**
     * @Description  组装AC01
     * @param: ac01PO
    * @param: bean
     * @return  void
     * @author  0214
     * @createTime 2018-09-20 13:07
     * @updateTime
     */
    void assembleAC01(Ac01PO ac01PO, BasicPersonInfoPO bean) throws Exception;



    int updateAC01Status(Ac01DAO acstatusBean);



    Result synchroFromExcel(SynchroExcelVO synchroExcelVO) throws Exception;
}
