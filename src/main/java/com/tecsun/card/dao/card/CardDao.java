package com.tecsun.card.dao.card;

import com.tecsun.card.controller.BatchBean;
import com.tecsun.card.entity.beandao.card.AZ01DAO;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.card.SysLogDAO;
import com.tecsun.card.entity.beandao.visualdata.ColumnDAO;
import com.tecsun.card.entity.beandao.visualdata.UserDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.AZ01PO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BusApplyPO;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

// ~ GET
// ~ INSERT
// ~ UPDATE
// ~ DELETE
@MapperScan
public interface CardDao {
    // ~ GET ---------------------------------------------

    /**
     * 根据name和idcard获取数量
     *
     * @return java.util.List<java.lang.String>
     * @Description
     * @param: idCard
     * @param: name
     * @author 0214
     * @createTime 2018-09-19 16:56
     * @updateTime
     */
    List<String> getUserByIdCardAndNameAll(@Param("idCard") String idCard, @Param("name") String name);

    /**
     * @return
     * @Description 获取AC01详细信息, 去重(获取ID最大的那条记录)
     * @params
     * @author 0214
     * @createTime 2018-09-14 16:47
     * @updateTime
     */
    Ac01PO getAC01DetailByIdCardAndName(@Param("idCard") String idCard, @Param("name") String name);

    /**
     * @return com.tecsun.card.entity.po.AZ01PO
     * @Description 通过userId获取卡详情
     * @param: cardId
     * @author 0214
     * @createTime 2018-09-25 10:47
     * @updateTime
     */
    AZ01PO getCardByUserId(@Param("cardId") Long cardId);

    /**
     * @return
     * @Description 获取卡管库所有人员ID和姓名
     * @param: null
     * @author 0214
     * @createTime 2018-09-25 10:46
     * @updateTime
     */
    List<Ac01DAO> listAllUserIdCardAndName();


    /**
     * @return java.util.List<com.tecsun.card.entity.po.Ac01PO>
     * @Description 通过区域ID、人员状态获取人员详情
     * @param: regionalCode
     * @param: userStatus
     * @author 0214
     * @createTime 2018-09-26 16:07
     * @updateTime
     */
    List<Ac01PO> getAC01DetailByRegeionalCodeAndStatus(@Param("regionalCode")String regionalCode, @Param("userStatus")String userStatus);

    // ~ UPDATE  ---------------------------------------------

    /**
     * 更新AC01人员表状态
     * 包括 人员状态status、申领状态apply_status
     *
     * @param Ac01DAO
     * @return
     */
    int updateAC01Status(Ac01DAO Ac01DAO);


    // ~ DELETE  ---------------------------------------------

    /**
     * 获取所有AC01表中的人员数据
     *
     * @return
     */
    List<Ac01PO> listAllAC01();


    // ~ INSERT  ---------------------------------------------

    /**
     * @return int
     * @Description 插入AC01表
     * @param: ac01PO
     * @author 0214
     * @createTime 2018-09-20 10:59
     * @updateTime
     */
    int insertUser(Ac01PO ac01PO);


    int insertBusApply(BusApplyPO busApplyPO);


    long getUserSeq();


    List<VisualDataDoughunDAO> getVDCollectAC01();

    List<BatchBean> getBatchBeanByIdCard(String idCard);

    List<Ac01DAO> getUserByIdCardAndName(@Param("idCard") String idCard, @Param("name") String name);

    int updateAZ01StatusByIdCardAndName(@Param("az01DAO") AZ01DAO az01DAO);

    int insertSysLog(SysLogDAO sysLogDAO);

    /**
     * 根据用户名获取当前用户所有的表
     *
     * @param userName
     * @return
     */
    UserDAO getUserTable(@Param("userName") String userName);

    /**
     * 根据表名获取表中所有列
     *
     * @param tableName
     * @return
     */
    List<ColumnDAO> getTableColumn(@Param("tableName") String tableName);

    /**
     * 根据表名获取表名注释
     *
     * @param tableName
     * @return
     */
    String getTableCommonByTableName(@Param("tableName") String tableName);

    /**
     * 根据表名和列名获取列名注释
     *
     * @param tableName
     * @param columnName
     * @return
     */
    String getColumnCommonByColumnName(@Param("tableName") String tableName, @Param("columnName") String columnName);

}
