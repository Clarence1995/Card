package com.tecsun.card.service.impl;

import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.excel.ExcelUtil;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.dao.collect.CollectDao;
import com.tecsun.card.entity.BasicInfoBean;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.AZ01PO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.entity.vo.SynchroExcelVO;
import com.tecsun.card.exception.MyException;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.RedisService;
import oracle.jdbc.driver.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

@Service("cardService")
public class CardServiceImpl implements CardService {
    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
    @Autowired
    CollectService collectService;
    @Resource
    private CardDao      cardDao;
    @Resource
    private CollectDao   collectDao;
    @Autowired
    private RedisService redisService;


    // ~ GET ------------------------------------------------

    /**
     * 获取AC01详细信息, 去重(获取ID最大的那条记录)
     *
     * @return com.tecsun.card.entity.po.Ac01PO
     * @param: idCard
     * @param: name
     * @author 0214
     * @createTime 2018-09-20 10:51
     * @updateTime
     */
    @Override
    public Ac01PO getAC01DetailByIdCardAndName(String idCard, String name) {
        return cardDao.getAC01DetailByIdCardAndName(idCard, name);
    }

    /**
     * @return boolean
     * @Description 根据用户名和身份证号获取用户(只返回身份证号, 但是不去重, 用于判断重复或是否存在此此用户)
     * @param: idCard
     * @param: name
     * @author 0214
     * @createTime 2018-09-20 10:52
     * @updateTime
     */
    @Override
    public boolean userExistJudgeByIdCardAndName(String idCard, String name) {
        List<String> resultList = cardDao.getUserByIdCardAndNameAll(idCard, name);
        return resultList.size() > 0;
    }

    /**
     * @return
     * @Description AC01个人编号生成
     * @params
     * @author 0214
     * @createTime 2018/9/6
     * @updateTime
     */
    @Override
    public String generateAC01IUserNumber() throws Exception {
        StringBuilder sb        = new StringBuilder(28);
        long          userNo    = 0;
        String        userNoStr = redisService.get(Constants.REDIS_USER_SERIAL_KEY);

        // redis中存在该值
        if ((ObjectUtils.isEmpty(userNoStr))) {
            userNo = this.getUserSeq();
            redisService.set(Constants.REDIS_USER_SERIAL_KEY, String.valueOf(userNo + 1));
        } else {
            userNo = Long.parseLong(userNoStr);
            redisService.set(Constants.REDIS_USER_SERIAL_KEY, String.valueOf(Long.parseLong(userNoStr) + 1));
        }
        String times = String.valueOf(System.currentTimeMillis());
        sb.append(times);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        // 补全卡号
        DecimalFormat df = new DecimalFormat("000000000");
        sb.append(df.format(userNo));
        logger.info("[0214 AC01用户编号] 编号为： " + sb.toString());
        return sb.toString();
    }

    /**
     * @return long
     * @Description 从数据库获取序列
     * @param:
     * @author 0214
     * @createTime 2018-09-20 10:39
     * @updateTime
     */
    @Override
    public long getUserSeq() {
        return cardDao.getUserSeq();
    }


    /**
     * @return com.tecsun.card.entity.po.AZ01PO
     * @Description 根据用户获取的CARD_ID 获取对应的Card信息
     * @param: cardId
     * @author 0214
     * @createTime 2018-09-20 10:38
     * @updateTime
     */
    @Override
    public AZ01PO getCardByUserId(Long cardId) {
        return cardDao.getCardByUserId(cardId);
    }

    /**
     * @return java.util.List<com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO>
     * @Description 可视化数据获取
     * @param:
     * @author 0214
     * @createTime 2018-09-20 10:53
     * @updateTime
     */
    @Override
    public List<VisualDataDoughunDAO> getVDCollectAC01() {
        return cardDao.getVDCollectAC01();
    }

    /**
     * @return java.util.List<com.tecsun.card.entity.beandao.card.Ac01DAO>
     * @Description 获取卡管库所有人员ID和姓名
     * @param:
     * @author 0214
     * @createTime 2018-09-25 10:45
     * @updateTime
     */
    @Override
    public List<Ac01DAO> listAllUserIdCard() {
        return cardDao.listAllUserIdCardAndName();
    }

    /**
     * @return java.util.List<com.tecsun.card.entity.po.Ac01PO>
     * @Description 通过区域ID、人员状态获取人员详情
     * @param: regionalCode
     * @param: userStatus
     * @author 0214
     * @createTime 2018-09-26 16:03
     * @updateTime
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public List<Ac01PO> getAC01DetailByRegeionalCodeAndStatus(String regionalCode, String userStatus) {
        return cardDao.getAC01DetailByRegeionalCodeAndStatus(regionalCode, userStatus);
    }


    // ~ UPDATE ------------------------------------------------

    // ~ DELETE ------------------------------------------------


    // ~ INSERT ------------------------------------------------

    /**
     * @return boolean
     * @Description AC01表插入
     * @param: ac01PO
     * @author 0214
     * @createTime 2018-09-20 10:57
     * @updateTime
     */
    @Override
    public boolean insertAC01(Ac01PO ac01PO) throws MyException {
        cardDao.insertUser(ac01PO);
        return true;
    }


    /**
     * @return boolean
     * @Description BUS_APPLY 申领表插入
     * @param: busApplyPO
     * @author 0214
     * @createTime 2018-09-20 11:01
     * @updateTime
     */
    @Override
    public boolean insertBusApply(BusApplyPO busApplyPO) {
        cardDao.insertBusApply(busApplyPO);
        return true;
    }


    /**
     * @return boolean
     * @Description BUS_APPLY 和 AC01 同时插入 == 新申领操作
     * @param: ac01bean
     * @param: basicBean
     * @author 0214
     * @createTime 2018-09-20 11:02
     * @updateTime
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public boolean insertCardAC01AndBusApplyFromCollect(Ac01PO ac01bean, BasicPersonInfoPO basicBean) {
        BusApplyPO busApplyPO = new BusApplyPO();
        // 01 新申领
        busApplyPO.setBusinessType(Constants.BUS_APLY_BUSINESSTYPE_NEW_APPLY);
        // 00 申请
        busApplyPO.setStatus(Constants.BUS_APPLY_STATUS_APPLY);
        // 01 个人申领
        busApplyPO.setSource(Constants.BUS_APPLY_SOURCE_PERSON);
        // 区域编码
        busApplyPO.setRegionalId(basicBean.getRegionalCode());
        busApplyPO.setApplyName(basicBean.getName());
        busApplyPO.setApplyIdCard(basicBean.getCertNum());
        busApplyPO.setApplyMobile(basicBean.getMobile());
        busApplyPO.setFlag(Constants.BUS_APPLY_FLAG_CHOOSE_NUM_NO);
        busApplyPO.setChooseCardNo("");
        // 00 不更换银行卡号
        busApplyPO.setChangeBankNo(Constants.BUS_APPLY_CHANGE_BANK_NUM_NO);
        // 申办人
        // 服务银行
        // 邮件地址
        String expressAddress = basicBean.getExpressAddress();
        if (null != expressAddress) {
            // 01 邮寄
            busApplyPO.setIsexpress(Constants.BUS_APPLY_EXPRESS_YES);
            busApplyPO.setExpressName(basicBean.getExpressName());
            busApplyPO.setExpressPhone(basicBean.getExpressPhone());
            busApplyPO.setExpressAddress(expressAddress);
        } else {
            // 00 不邮寄
            busApplyPO.setIsexpress(Constants.BUS_APPLY_EXPRESS_NO);
        }

        // 新增 AC01
        try {
            this.insertAC01(ac01bean);
            // 获取人员ID
            busApplyPO.setPersonId(ac01bean.getIdKey());
            // BUS_APPLY, 新增申领表记录
            this.insertBusApply(busApplyPO);

            // 回写采集库
            CollectVO collectVO = new CollectVO();
            collectVO.setCertNum(ac01bean.getAac147());
            collectVO.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
            collectVO.setDealStaus(Constants.COLLECT_QUALIFIED);
            collectVO.setDealMsg("");
            collectService.updateUserInfoStatusByIdCardAndName(collectVO);
        } catch (Exception e) {
            logger.error("[0214 数据同步出错,现进行回滚操作。原因为{}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        logger.info("[0214 采集同步] 人员ID: {} 已成功同步至卡管库", ac01bean.getAac147());
        return true;
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public int updateAC01Status(Ac01DAO acstatusBean) {
        return cardDao.updateAC01Status(acstatusBean);
    }


    @Override
    public Result synchroFromExcel(SynchroExcelVO synchroExcelVO) throws Exception {
        String filePath = "D:\\aaaa.xlsx";
        // 1、读取Excel表
        List<BasicInfoBean> excelBeanList = new ExcelUtil<BasicInfoBean>(new BasicInfoBean()).readFromFile(null, new File(filePath), null);
        // 2、循环遍历,判断
        return null;
    }

    // ~ ELSE ------------------------------------------------

    /**
     * @return void
     * @Description 根据从采集库获取的人员详情, 组装人员表
     * @param: ac01
     * @param: bean
     * @author 0214
     * @createTime 2018-09-20 10:35
     * @updateTime
     */
    @Override
    public void assembleAC01(Ac01PO ac01, BasicPersonInfoPO bean) throws Exception {
        // 个人编号
        ac01.setAac001(this.generateAC01IUserNumber());
        // 单位编号
        if (ObjectUtils.notEmpty(bean.getDepartmentNo())) {
            ac01.setAab001(bean.getDepartmentNo().trim());
        }
        // 部门名称
        if (ObjectUtils.notEmpty(bean.getDepartmentName())) {
            ac01.setAab004(bean.getDepartmentName().trim());
        }
        // 证件有效期,可能会出现中文 " 长期" 如果出现,则设置为20991231
        if (null != bean.getCertValidity() && "长期".equals(bean.getCertValidity())) {
            ac01.setAab099("20991231");
        } else if (null != bean.getCertValidity()) {
            ac01.setAab099(bean.getCertValidity().trim());
        }
        // 社会保障号=身份证号
        ac01.setAac002(bean.getCertNum().trim());
        // 姓名
        ac01.setAac003(bean.getName().trim());
        // 藏文名字
        ac01.setAac003a(bean.getZangName());
        // 性别
        if (ObjectUtils.notEmpty(bean.getSex())) {
            ac01.setAac004(bean.getSex());
        }
        // 民族
        ac01.setAac005(bean.getNation());
        // 出生年月
        ac01.setAac006(bean.getBirthday());
        // 户口性质
        if (ObjectUtils.notEmpty(bean.getHkProperty())) {
            ac01.setAac009(bean.getHkProperty().trim());
        }
        // 户口所在地
        ac01.setAac010(bean.getAddress().trim());
        // 个人身份
        if (ObjectUtils.notEmpty(bean.getPersonStatus())) {
            ac01.setAac012(bean.getPersonStatus().trim());
        }
        // 监护人姓名，年龄小于16周岁的人员，此项必填
        if (ObjectUtils.notEmpty(bean.getGuardianName())) {
            ac01.setAac042(bean.getGuardianName().trim());
        }
        // 监护人证件号码，年龄小于16周岁的人员，此项必填
        if (ObjectUtils.notEmpty(bean.getGuardianCertno())) {
            ac01.setAac044(bean.getGuardianCertno().trim());
            ac01.setAac043(Constants.IDCARD_TYPE_JUMING);
        }
        // 证件类型
        ac01.setAac058(bean.getCertType());
        // 生存状态
        ac01.setAac060(Constants.USER_AAC060_ALIVE);
        // 电话号码
        ac01.setAac067(bean.getMobile());
        // 身份证号
        ac01.setAac147(bean.getCertNum().trim());
        // 国籍
        ac01.setAac161(bean.getGuoJi().trim());
        // 固定号码
        if (ObjectUtils.notEmpty(bean.getPhone())) {
            ac01.setAae005(bean.getPhone().trim());
        }
        // 常住地
        if (ObjectUtils.notEmpty(bean.getParmanentAddress())) {
            ac01.setAae006(bean.getParmanentAddress().trim());
        }
        // 常住地邮编
        if (ObjectUtils.notEmpty(bean.getParmanentZipCode())) {
            ac01.setAae007(bean.getParmanentZipCode());
        } else {
            ac01.setAae007(Constants.USUAL_ADDRESS_CODE);
        }
        // 职业(其他)
        ac01.setAca111("");
        // 申领状态 00 默认
        ac01.setApplyStatus(Constants.USER_APPLY_STATUS_DEFAULT);
        // 申领类型 00 新申领
        ac01.setApplyType(Constants.USER_APPLY_TYPE_NEW_APPLY);
        // 操作人Id
        ac01.setCreateOperatorId("0");
        // EMAIL
        ac01.setEmail("");
        // 标记状态 0 正常
        ac01.setMarkStatus(Constants.USER_MARK_STATUS_NORMAL);
        // 照片路径
        ac01.setPhotourl(bean.getPhotoUrl());
        // 人员状态 00 初始
        ac01.setStatus(Constants.USER_STATUS_INIT);
        // 有效标志 00 有效
        ac01.setValidtag(Constants.USER_VALIDTAG_VALID);
        // 采集标志 00 采集库
        ac01.setCollectFlag(Constants.USER_COLLECT_FLAG_COLLECT);
    }

}
