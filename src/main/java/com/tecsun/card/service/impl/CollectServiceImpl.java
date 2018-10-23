package com.tecsun.card.service.impl;

import com.tecsun.card.common.clarencezeroutils.HandleCollectDateUtils;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.dao.collect.CollectDao;
import com.tecsun.card.dao.midtwenty.MidTwenty;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("collectService")
public class CollectServiceImpl implements CollectService {
    public static void main(String[] args) {
        BasicPersonInfoDAO basicBeanDao = new BasicPersonInfoDAO();
        CollectVO          collectVO    = new CollectVO();
        basicBeanDao.setDongRuanSynchroStatus(collectVO.getDongRuanSynchroStatus());
        System.out.println(collectVO);
    }

    private final static Logger     logger       = LoggerFactory.getLogger(CollectServiceImpl.class);
    private static       int        SUCCESS_CODE = 200;
    private static       int        FAIL_CODE    = 0;
    @Resource
    private              CollectDao collectDao;

    @Autowired
    private CardService cardService;

    @Autowired
    private MidTwenty midTwenty;

    // ~ GET

    /**
     * @return
     * @Description 根据IDCard和姓名获取采集表中人员详情, 并且获取ID最大的那一条记录
     * @params
     * @author 0214
     * @createTime 2018-09-14 17:13
     * @updateTime
     */
    @Override
    public BasicPersonInfo getBasicInfoByIDCard(String idCard, String name) throws Exception {
        if (null == idCard) {
            throw new NullPointerException("[0214] 获取40采集库人员数据出错,入参身份证号码不能为空");
        }
        return collectDao.getSingleBasicPersonByIdcard(idCard, name);
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Description 获取采集库人员重复数据
     * @param:
     * @author 0214
     * @createTime 2018-09-21 15:38
     * @updateTime
     */
    @Override
    public List<String> getUserRepeatIdCard() {
        return collectDao.getUserRepeatIdCard();
    }


    /**
     * @param idCard
     * @param name
     * @return java.util.List<com.tecsun.card.entity.po.BasicPersonInfo>
     * @Description 获取人员信息详情(包含重复数据)
     * @param:
     * @author 0214
     * @createTime 2018-09-21 15:50
     * @updateTime
     */
    @Override
    public List<BasicPersonInfo> getUserInfoWithRepeat(String idCard, String name) {
        return collectDao.getUserInfoWithRepeat(idCard, name);
    }


    /**
     * @return java.util.List<java.lang.String>
     * @Description 获取所有采集表的IdCard
     * @param:
     * @author 0214
     * @createTime 2018-09-25 10:32
     * @updateTime
     */
    @Override
    public List<BasicPersonInfoDAO> listAllUserIDCard() {
        return collectDao.listAllUserIDCardAndName();
    }

    @Override
    public BasicPersonInfo getSingleBasicPersonByIdcardFromMidTwenty(String idCard, String name) throws Exception {
        if (null == idCard) {
            throw new NullPointerException("[0214] 获取20中间库人员采集信息出错,idCard不能为空");
        }
        return midTwenty.getSingleBasicPersonByIdcardFromMidTwenty(idCard, name);
    }
    // ~ UPDATE

    /**
     * 更新采集库BASIC_PERSON_INFO的人员状态(回写)
     *
     * @param basicDao
     * @return
     */
    @Override
    public int updateUserInfoStatusByIdCardAndName(CollectVO basicDao) throws Exception {
        // 组装DAO层类
        BasicPersonInfoDAO basicBeanDao = new BasicPersonInfoDAO();
        if (null == basicBeanDao) {
            throw new  NullPointerException("[0214] 采集库人员信息更新出错, 入参不能为空");
        }
        if (null == basicDao.getCertNum()) {
            throw new NullPointerException("[0214] 采集库人员信息更新出错, 入参 身份证号码为空,不允许更新操作");
        }
        basicBeanDao.setCertNum(basicDao.getCertNum());
        basicBeanDao.setName(basicDao.getName());
        basicBeanDao.setDealStatus(basicDao.getDealStaus());
        basicBeanDao.setSynchroStatus(basicDao.getSynchroStatus());
        basicBeanDao.setDealMsg(basicDao.getDealMsg());
        basicBeanDao.setDepartmentName(basicDao.getDepartmentName());
        basicBeanDao.setDepartmentNo(basicDao.getDepartmentNo());
        basicBeanDao.setDongRuanSynchroStatus(basicDao.getDongRuanSynchroStatus());
        int a = collectDao.updateUserInfoStatusByIdCardAndName(basicBeanDao);
        return a;
    }

    /**
     * @return int
     * @Description 通过IdList更新采集库人员状态, 包括人员同步状态、处理状态、处理信息
     * @param: successIdList
     * @param: synchroStatus
     * @param: dealStatus
     * @param: dealMsg
     * @author 0214
     * @createTime 2018-09-21 16:40
     * @updateTime
     */
    @Override
    public int updateUserInfoStatusByIdList(List<Long> successIdList, String synchroStatus, String dealStatus, String dealMsg) {
        if (!ObjectUtils.notEmpty(successIdList)) {
            throw new NullPointerException("[0214 采集人员表通过IDList更新出错, idList为空");
        }
        BasicPersonInfoDAO userInfo = new BasicPersonInfoDAO();
        userInfo.setIdList(successIdList);
        userInfo.setSynchroStatus(synchroStatus);
        userInfo.setDealMsg(dealMsg);
        userInfo.setDealStatus(dealStatus);
        return collectDao.updateUserInfoStatusByIdList(userInfo);
    }

    // ~ DELETE
    // ~ INSERT


    @Override
    public List<BasicPersonInfo> listAllBasicPersonInfoPO() {
        return collectDao.lisetBasicPersonInfo();
    }


    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public List<BasicPersonInfo> listQualifiedBasicPerson(CollectVO collectVO) {
        BasicPersonInfoDAO basicBeanDao = new BasicPersonInfoDAO();
        basicBeanDao.setCertNum(collectVO.getCertNum());
        basicBeanDao.setSynchroStatus(collectVO.getSynchroStatus());
        basicBeanDao.setRegionalCode(collectVO.getRegionalCode());
        return collectDao.listQualifiedBasicPerson(basicBeanDao);
    }

    @Override
    public String getZangNameByIdCard(String idCard) {
        return collectDao.getZangName(idCard);
    }


    /**
     * @return
     * @Description 用户信息校验, 包括用户基本信息、是否为婴儿卡、区域信息校验
     * @params
     * @author 0214
     * @createTime 2018-09-17 11:32
     * @updateTime
     */
    @Override
    public boolean validateuserInfo(BasicPersonInfo basicBean) {
        boolean       validateResult = true;
        StringBuilder dealMsg        = new StringBuilder();
        dealMsg.append("基本信息缺失如下: ");
        if (StringUtils.isBlank(basicBean.getCertNum())) {
            validateResult = false;
            basicBean.setDealMsg("身份证号码为空");
            return validateResult;
        }
        if (StringUtils.isBlank(basicBean.getRegionalCode())) {
            // 行政区划代码
            validateResult = false;
            basicBean.setDealMsg(",行政区划代码为空");
            return validateResult;
        }
        if (StringUtils.isBlank(basicBean.getName())) {
            validateResult = false;
            dealMsg.append(",姓名为空");
        }
        if (StringUtils.isBlank(basicBean.getSex())) {
            validateResult = false;
            dealMsg.append(",性别为空,");
        }
        if (StringUtils.isBlank(basicBean.getNation())) {
            validateResult = false;
            dealMsg.append(",民族为空");
        }
        if (StringUtils.isBlank(basicBean.getGuoJi())) {
            validateResult = false;
            dealMsg.append(",国籍为空");
        }
        if (StringUtils.isBlank(basicBean.getAddress())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(",户籍地址为空");
        }
        if (StringUtils.isBlank(basicBean.getCertValidity())) {
            // 证件有效期
            validateResult = false;
            dealMsg.append(",证件有效期为空");
        }
        if (StringUtils.isBlank(basicBean.getParmanentAddress())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(",常住地址为空");
        }
        if (StringUtils.isBlank(basicBean.getCertType())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(",证件类型为空");
        }
        if (StringUtils.isBlank(basicBean.getMobile())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(",手机号为空");
        }
        if (StringUtils.isBlank(basicBean.getBirthday())) {
            basicBean.setBirthday(basicBean.getCertNum().substring(6, 14));
        }

        // 2、判断是否为婴儿卡
        boolean eBabyCard = HandleCollectDateUtils.eBabyCard(basicBean.getBirthday());
        if (eBabyCard) {
            basicBean.setIsBaby(Constants.COLLECT_IS_BABY);
        } else {
            basicBean.setIsBaby(Constants.COLLECT_NOT_BABY);
        }
        // 3、区域信息校验
        Result validateRegionalcodeR = HandleCollectDateUtils.validateRegionalCode(basicBean.getRegionalCode());
        int    validCodeR            = validateRegionalcodeR.getStateCode();
        if (SUCCESS_CODE == validCodeR) {
            basicBean.setDealMsg("");
            basicBean.setDealstatus(Constants.COLLECT_QUALIFIED);
        } else {
            basicBean.setDealMsg(validateRegionalcodeR.getMsg());
            basicBean.setDealMsg(Constants.COLLECT_USERINFO_ERROR);
        }
        return validateResult;
    }

    @Override
    public List<BasicPersonInfo> getBasicInfoFromList(List<String> idCardList) {
        return collectDao.listBasicBeanByIdList(idCardList);
    }

    @Override
    public List<VisualDataDoughunDAO> getVDBasicPersonAnalyse() {
        return collectDao.listVDBasicPersonAnalyset();
    }


    @Override
    public int getCountFromBasicInfo(String idCard, String name) {
        return collectDao.getBasicPersonByIdCardAndName(idCard, name);
    }


}
