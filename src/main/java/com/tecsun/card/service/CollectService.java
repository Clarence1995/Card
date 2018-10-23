package com.tecsun.card.service;

import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.vo.CollectVO;

import java.util.List;

public interface CollectService {
    // ~-----------------------------------
    // ~------------- GET START 20180927----------------------

    /**
     * @return java.lang.String
     * @Description 通过IdCard获取藏文名字
     * @param: idCard
     * @author 0214
     * @createTime 2018-09-20 10:32
     * @updateTime
     */
    String getZangNameByIdCard(String idCard);

    /**
     * @return java.util.List<java.lang.String>
     * @Description 获取采集库人员重复数据
     * @param:
     * @author 0214
     * @createTime 2018-09-21 15:37
     * @updateTime
     */
    List<String> getUserRepeatIdCard();

    /**
     * @param idCard
     * @param name
     * @return java.util.List<com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO>
     * @Description 获取人员信息详情(包含重复数据)
     * @param:
     * @author 0214
     * @createTime 2018-09-21 15:48
     * @updateTime
     */
    List<BasicPersonInfo> getUserInfoWithRepeat(String idCard, String name);

    /**
     * @return java.util.List<com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO>
     * @Description 获取所有采集表的IdCard
     * @param:
     * @author 0214
     * @createTime 2018-09-25 10:38
     * @updateTime
     */
    List<BasicPersonInfoDAO> listAllUserIDCard();

    /**
     * @return java.util.List<com.tecsun.card.entity.po.BasicPersonInfo>
     * @Description 从中间表20获取区外人员信息
     * @param: idCard
     * @param: name
     * @author 0214
     * @createTime 2018-09-27 09:13
     * @updateTime
     */
    BasicPersonInfo getSingleBasicPersonByIdcardFromMidTwenty(String idCard, String name) throws Exception;

    // ~ UPDATE

    /**
     * @return int
     * @Description 更新采集库BASIC_PERSON_INFO的人员状态(回写)
     * @param: CollectVO
     * @author 0214
     * @createTime 2018-09-19 17:01
     * @updateTime
     */
    int updateUserInfoStatusByIdCardAndName(CollectVO CollectVO) throws Exception;


    /**
     * @return int
     * @Description 通过IdList更新采集库人员状态, 包括人员同步状态、处理状态、处理信息
     * @param: successIdList
     * @author 0214
     * @createTime 2018-09-21 16:33
     * @updateTime
     */
    int updateUserInfoStatusByIdList(List<Long> successIdList, String synchroStatus, String dealStatus, String dealMsg);


    // ~ DELETE ------------------------------------

    // ~ INSERT

    /**
     * @return
     * @Description 根据IDCard和姓名获取采集表中人员详情
     * @params
     * @author 0214
     * @createTime 2018-09-14 17:13
     * @updateTime
     */
    BasicPersonInfo getBasicInfoByIDCard(String idCard, String name) throws Exception;

    /**
     * @return
     * @Description 采集表基础信息校验
     * @params
     * @author 0214
     * @createTime 2018-09-14 17:34
     * @updateTime
     */
    boolean validateuserInfo(BasicPersonInfo basicBean);

    List<BasicPersonInfo> listAllBasicPersonInfoPO();


    List<BasicPersonInfo> listQualifiedBasicPerson(CollectVO synchroStatus);


    List<BasicPersonInfo> getBasicInfoFromList(List<String> idCardList);

    List<VisualDataDoughunDAO> getVDBasicPersonAnalyse();

    int getCountFromBasicInfo(String idCard, String name);

}
