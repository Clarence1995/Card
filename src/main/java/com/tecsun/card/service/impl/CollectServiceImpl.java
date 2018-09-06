package com.tecsun.card.service.impl;

import com.tecsun.card.dao.collect.CollectDao;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("collectService")
public class CollectServiceImpl implements CollectService {
    private final static Logger logger = LoggerFactory.getLogger(CollectServiceImpl.class);

    @Resource
    private CollectDao collectDao;

    @Autowired
    private CardService cardService;

    @Override
    public List<BasicPersonInfoPO> listAllBasicPersonInfoPO () {
        return collectDao.lisetBasicPersonInfo();
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = { Exception.class })
    public int updateBasicPersonInfoStatus (CollectVO basicDao) {
        BasicPersonInfoDAO basicBeanDao = new BasicPersonInfoDAO();
        basicBeanDao.setCertNum(basicDao.getCertNum());
        basicBeanDao.setDealStatus(basicDao.getDealStaus());
        basicBeanDao.setSynchroStatus(basicDao.getSynchroStatus());
        basicBeanDao.setDealMsg(basicDao.getDealMsg());
        int a = collectDao.updateBasicPersonInfoStatus(basicBeanDao);
        return a;
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = { Exception.class })
    public List<BasicPersonInfoPO> listQualifiedBasicPerson (CollectVO collectVO) {
        BasicPersonInfoDAO basicBeanDao = new BasicPersonInfoDAO();
        basicBeanDao.setCertNum(collectVO.getCertNum());
        basicBeanDao.setSynchroStatus(collectVO.getSynchroStatus());
        basicBeanDao.setRegionalCode(collectVO.getRegionalCode());
        return collectDao.listQualifiedBasicPerson(basicBeanDao);
    }

    @Override
    public String getZangName (String idCard) {
        return collectDao.getZangName(idCard);
    }


    /**
     * 采集库入卡管人员信息校验,并把校验信息写入dealMsg
     * 1、基础信息是否为空校验
     * 2、婴儿卡校验
     * 3、区域信息校验
     * @param basicBean
     * @return
     */
    @Override
    public boolean validateBasicPersonInfo (BasicPersonInfoPO basicBean) {
        boolean validateResult = true;
        StringBuilder dealMsg = new StringBuilder();
        dealMsg.append("基本信息缺失: ");
        if (StringUtils.isBlank(basicBean.getCertNum())) {
            validateResult = false;
            basicBean.setDealMsg("身份证号码为空");
            return validateResult;
        }
        if (StringUtils.isBlank(basicBean.getRegionalCode())) {
            // 行政区划代码
            validateResult = false;
            basicBean.setDealMsg("行政区划代码为空");
            return validateResult;
        }
        if (StringUtils.isBlank(basicBean.getName())) {
            validateResult = false;
            dealMsg.append("姓名为空,");
        }
        if (StringUtils.isBlank(basicBean.getSex())) {
            validateResult = false;
            dealMsg.append("性别为空,");
        }
        if (StringUtils.isBlank(basicBean.getNation())) {
            validateResult = false;
            dealMsg.append("民族为空,");
        }
        if (StringUtils.isBlank(basicBean.getGuoJi())) {
            validateResult = false;
            dealMsg.append("国籍为空,");
        }
        if (StringUtils.isBlank(basicBean.getAddress())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append("户籍地址为空,");
        }
        if (StringUtils.isBlank(basicBean.getCertValidity())) {
            // 证件有效期
            validateResult = false;
            dealMsg.append("证件有效期为空,");
        }
        if (StringUtils.isBlank(basicBean.getParmanentAddress())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append("常住地址为空,");
        }
        if (StringUtils.isBlank(basicBean.getCertType())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append("证件类型为空,");
        }
        if (StringUtils.isBlank(basicBean.getMobile())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append("手机号为空,");
        }

        // 2、判断是否为婴儿卡
        String certNum = basicBean.getCertNum();
        String year = certNum.substring(6, 10);
        String month = certNum.substring(10, 12);
        String day = certNum.substring(12, 14);
        String birthday = year + "/" + month + "/" + day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date birth = sdf.parse(birthday);
            Date now = new Date();
            // 60*60*24*365*1000
            boolean result = (now.getTime() - birth.getTime()) < 3600L * 24L * 365L * 6L * 1000L ? true : false;
            if (result) {
                basicBean.setIsBaby(Constants.COLLECT_IS_BABY);
            } {
                basicBean.setIsBaby(Constants.COLLECT_NOT_BABY);
            }
        } catch (ParseException e) {
			e.printStackTrace();
        }

        // 3、区域信息校验
        String regionalCode = basicBean.getRegionalCode();
        if (regionalCode.length() == 6 || regionalCode.length() == 8) {
            if (regionalCode.length() == 6) {
                if (!Constants.OUTERCODELIST.contains(regionalCode)) {
                    logger.info("[采集库 信息校验] 此人:{} 区划编码不属于区外异地", basicBean.getCertNum());
                    dealMsg.append("区划编码长度为6位,并不属于区外异地");
                    validateResult = false;
                }
            } else {
                if (!(regionalCode.length() == 8 && (regionalCode.substring(0, 2).equals("54")))) {
                    logger.info("[采集库 信息校验] 此人:{} 区划编码不属于区内编码 ", basicBean.getCertNum());
                    dealMsg.append("区划编码长度为8位,并不属于区内编码");
                    validateResult = false;
                }
            }
        } else {
            logger.info("[采集库 信息校验] 此人: {} 区划编码不为6位或8位", basicBean.getCertNum());
            dealMsg.append("区划编码长度不为6位或8位");
            validateResult = false;
        }
        if (validateResult) {
            basicBean.setDealMsg("");
        } else {
            basicBean.setDealMsg(dealMsg.toString());
        }
        return validateResult;
    }

    @Override
    public List<BasicPersonInfoPO> getBasicInfoFromList (List<String> idCardList) {
        return  collectDao.listBasicBeanByIdList(idCardList);
    }

    @Override
    public List<VisualDataDoughunDAO> getVDBasicPersonAnalyse () {
        return collectDao.listVDBasicPersonAnalyset();
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = { Exception.class })
    public BasicPersonInfoPO getBasicInfoByIDCard(String idCard) {
        return collectDao.getBasicPersonByIdCard(idCard);
    }


}
