package com.tecsun.card.dao.collect;

import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface CollectDao {
    // ~ GET

    /**
     * @return
     * @Description 根据姓名、身份证获取人员详情(id最大值)
     * @params
     * @author 0214
     * @createTime 2018-09-14 17:21
     * @updateTime
     */
    BasicPersonInfoPO getSingleBasicPersonByIdcard(@Param("idCard") String idCard, @Param("name") String name);


    /**
     * @Description 获取采集库人员重复数据
     * @param:
     * @return  java.util.List<java.lang.String>
     * @author  0214
     * @createTime 2018-09-21 15:38
     * @updateTime
     */
    List<String> getUserRepeatIdCard();


    /**
     * @param idCard
     * @param name
     * @return java.util.List<com.tecsun.card.entity.po.BasicPersonInfoPO>
     * @Description 获取人员信息详情(包含重复数据)
     * @param:
     * @author 0214
     * @createTime 2018-09-21 15:50
     * @updateTime
     */
    List<BasicPersonInfoPO> getUserInfoWithRepeat(@Param("idCard")String idCard, @Param("name")String name);


    /***
     * @Description
     * @param:
     * @return  java.util.List<java.lang.String>
     * @author  0214
     * @createTime 2018-09-25 10:32
     * @updateTime
     */
    List<BasicPersonInfoDAO> listAllUserIDCardAndName();
    // ~ UPDATE

    /**
     * @return int
     * @Description
     * @param: basicDao
     * @author 0214
     * @createTime 2018-09-20 10:05
     * @updateTime
     */
    int updateUserInfoStatusByIdCardAndName(BasicPersonInfoDAO basicDao);

    /**
     * @Description 通过IdList更新采集库人员状态, 包括人员同步状态、处理状态、处理信息
     * @param: userInfo
     * @return  int
     * @author  0214
     * @createTime 2018-09-21 16:40
     * @updateTime
     */
    int updateUserInfoStatusByIdList(BasicPersonInfoDAO userInfo);


    // ~ DELETE


    // ~ INSERT


    List<BasicPersonInfoPO> lisetBasicPersonInfo();


    List<BasicPersonInfoPO> listQualifiedBasicPerson(BasicPersonInfoDAO basicPersonInfoDAO);

    String getZangName(String idCard);

    List<BasicPersonInfoPO> listBasicBeanByIdList(List<String> idCardList);

    List<VisualDataDoughunDAO> listVDBasicPersonAnalyset();


    /**
     * 根据姓名、身份证号获取采集表中人员数
     *
     * @param idCard   姓名
     * @param username 身份证号获
     * @return
     */
    Integer getBasicPersonByIdCardAndName(@Param("idCard") String idCard, @Param("username") String username);


}
