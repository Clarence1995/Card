package com.tecsun.card.dao.card;

import com.tecsun.card.controller.BatchBean;
import com.tecsun.card.entity.beandao.card.AZ01DAO;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.card.SysLogDAO;
import com.tecsun.card.entity.beandao.visualdata.ColumnDAO;
import com.tecsun.card.entity.beandao.visualdata.UserDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
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
    // ~ INSERT

    // ~ DELETE

    /**
     * 获取所有AC01表中的人员数据
     * @return
     */
    List<Ac01PO> listAllAC01();

    // ~ UPDATE

    /**
     * 更新AC01人员表状态
     * 包括 人员状态status、申领状态apply_status
     * @param Ac01DAO
     * @return
     */
    int updateAC01Status (Ac01DAO Ac01DAO);




    // ~ GET





    List<String> userExistInCardByIdCard (String idCard);

    int insertUser (Ac01PO ac01PO);

    int insertBusApply (BusApplyPO busApplyPO);

    long getUserSeq ();

    Ac01PO getUserByIdCard (String idCard);

    List<VisualDataDoughunDAO> getVDCollectAC01 ();

    List<BatchBean> getBatchBeanByIdCard (String idCard);

    List<Ac01DAO> getUserByIdCardAndName(@Param("idCard")String idCard, @Param("name")String name);

    int updateAZ01StatusByIdCardAndName(@Param("az01DAO")AZ01DAO az01DAO);

    int insertSysLog(SysLogDAO sysLogDAO);

    /**
     * 根据用户名获取当前用户所有的表
     * @param userName
     * @return
     */
    UserDAO getUserTable(@Param("userName")String userName);

    /**
     * 根据表名获取表中所有列
     * @param tableName
     * @return
     */
    List<ColumnDAO> getTableColumn(@Param("tableName")String tableName);

    /**
     * 根据表名获取表名注释
     * @param tableName
     * @return
     */
    String getTableCommonByTableName(@Param("tableName")String tableName);

    /**
     * 根据表名和列名获取列名注释
     * @param tableName
     * @param columnName
     * @return
     */
    String getColumnCommonByColumnName(@Param("tableName")String tableName, @Param("columnName")String columnName);
}
