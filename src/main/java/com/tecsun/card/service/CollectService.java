package com.tecsun.card.service;

import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;

import java.util.List;

public interface CollectService {
    // ~ GET
    /**
     * @Description 通过IdCard获取藏文名字
     * @param: idCard
     * @return  java.lang.String
     * @author  0214
     * @createTime 2018-09-20 10:32
     * @updateTime
     */
    String getZangNameByIdCard(String idCard);

    /**
     * @Description 获取采集库人员重复数据
     * @param:
     * @return  java.util.List<java.lang.String>
     * @author  0214
     * @createTime 2018-09-21 15:37
     * @updateTime
     */
    List<String> getUserRepeatIdCard();

    /**
     * @Description 获取人员信息详情(包含重复数据)
     * @param:
     * @return  java.util.List<com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO>
     * @author  0214
     * @createTime 2018-09-21 15:48
     * @updateTime
     * @param idCard
     * @param name
     */
    List<BasicPersonInfoPO> getUserInfoWithRepeat(String idCard, String name);


    // ~ UPDATE
    /**
     * @Description 更新采集库BASIC_PERSON_INFO的人员状态(回写)
     * @param: CollectVO
     * @return  int
     * @author  0214
     * @createTime 2018-09-19 17:01
     * @updateTime
     */
    int updateUserInfoStatusByIdCardAndName(CollectVO CollectVO);


    /**
     * @Description  通过IdList更新采集库人员状态,包括人员同步状态、处理状态、处理信息
     * @param: successIdList
     * @return  int
     * @author  0214
     * @createTime 2018-09-21 16:33
     * @updateTime
     */
    int updateUserInfoStatusByIdList(List<Long> successIdList, String synchroStatus, String dealStatus, String dealMsg);




    // ~ DELETE

    // ~ INSERT
    /**
     *@Description 根据IDCard和姓名获取采集表中人员详情
     *@params
     *@return
     *@author  0214
     *@createTime 2018-09-14 17:13
     *@updateTime
     */
    BasicPersonInfoPO getBasicInfoByIDCard(String idCard, String name);

    /**
     *@Description 采集表基础信息校验
     *@params
     *@return
     *@author  0214
     *@createTime 2018-09-14 17:34
     *@updateTime
     */
    boolean validateuserInfo(BasicPersonInfoPO basicBean);

    List<BasicPersonInfoPO> listAllBasicPersonInfoPO();


    List<BasicPersonInfoPO> listQualifiedBasicPerson (CollectVO synchroStatus);


    List<BasicPersonInfoPO> getBasicInfoFromList (List<String> idCardList);

    List<VisualDataDoughunDAO> getVDBasicPersonAnalyse ();

    int getCountFromBasicInfo(String idCard, String name);

}
