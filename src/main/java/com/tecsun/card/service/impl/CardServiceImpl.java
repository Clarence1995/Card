package com.tecsun.card.service.impl;

import com.tecsun.card.common.excel.ExcelUtil;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.dao.collect.CollectDao;
import com.tecsun.card.entity.BasicInfoBean;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.entity.vo.SynchroExcelVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private CardDao    cardDao;
    @Resource
    private CollectDao collectDao;
    @Autowired
    private RedisService redisService;

    @Override
    public List<Ac01PO> listAllAc01() {
        // return cardDao.listAllAC01();
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public int updateAC01Status(Ac01DAO acstatusBean) {
        return cardDao.updateAC01Status(acstatusBean);
    }

    @Override
    public boolean userExistInCard(String idCard, String name) {
        List<String> resultList = cardDao.userExistInCardByIdCard(idCard, name);
        if (null != resultList && !resultList.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean insertAC01(Ac01PO ac01PO) {
        try {
            cardDao.insertUser(ac01PO);
        } catch (Exception e) {
            logger.error("数据发生异常,进行回滚操作");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public boolean insertCardAC01AndBusApplyFromCollect(Ac01PO ac01bean, BasicPersonInfoPO basicBean) {
        try {
            BusApplyPO busApplyPO = new BusApplyPO();
            // 01 新申领
            busApplyPO.setBusinessType(Constants.BUSINESS_TYPE_01);
            // 00 申领状态: 申请
            busApplyPO.setStatus(Constants.APPLY_STATUS_00);
            // 01 个人申领
            busApplyPO.setSource("00");
            // 区域编码
            busApplyPO.setRegionalId(basicBean.getRegionalCode());
            busApplyPO.setApplyName(basicBean.getName());
            busApplyPO.setApplyIdCard(basicBean.getCertNum());
            busApplyPO.setApplyMobile(basicBean.getMobile());
            busApplyPO.setFlag("00");
            busApplyPO.setChooseCardNo("");
            busApplyPO.setChangeBankNo("00");
            // 申办人
            // 服务银行
            // 邮件地址
            String expressAddress = basicBean.getExpressAddress();
            if (null != expressAddress) {
                busApplyPO.setIsexpress("01");
                busApplyPO.setExpressName(basicBean.getExpressName());
                busApplyPO.setExpressPhone(basicBean.getExpressPhone());
                busApplyPO.setExpressAddress(expressAddress);
            } else {
                busApplyPO.setIsexpress("00");
            }

            // AC01
            this.insertAC01(ac01bean);
            // 人员ID
            busApplyPO.setPersonId(ac01bean.getIdKey());
            // BUS_APPLY, 新增申领表记录
            this.insertBusApply(busApplyPO);

            //回写采集库
            CollectVO collectVO = new CollectVO();
            collectVO.setCertNum(ac01bean.getAac147());
            collectVO.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
            collectVO.setDealStaus(Constants.COLLECT_QUALIFIED);
            collectService.updateBasicPersonInfoStatus(collectVO);
            logger.info("[采集库 => 卡管库] IDCard: {} 已成功完成同步", ac01bean.getAac147());
            return true;
        } catch (Exception e) {
            logger.error("数据发生异常,进行回滚操作");
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = { Exception.class })
    public boolean insertBusApply(BusApplyPO busApplyPO) {
        try {
            cardDao.insertBusApply(busApplyPO);
            return false;
        } catch (Exception e) {
            logger.error("数据发生异常,进行回滚操作");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return true;
    }

    /**
     *@Description ac01个人编号生成
     *@params
     *@return
     *@author  0214
     *@createTime  2018/9/6
     *@updateTime
     */
    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = { Exception.class })
    public String generateAC01IUserNumber() throws Exception {
        String result = "";
        long   userNo = 0;
        String ran    = "";
        String userNoStr = redisService.get("sisp:cardmanagement:user_serial");

        // redis中存在该值
        if (null == userNoStr || "".equals(userNoStr)) {
            userNo = this.getUserSeq();
            redisService.set(Constants.USER_SERIAL, String.valueOf(userNo + 1));
        } else {
            redisService.set(Constants.USER_SERIAL, String.valueOf(Long.parseLong(userNoStr) + 1));
        }
        String times  = String.valueOf(System.currentTimeMillis());
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            ran += random.nextInt(10);
        }
        result = times + ran;
        // 补全卡号
        DecimalFormat df = new DecimalFormat("000000000");
        result += df.format(userNo);
        System.out.println("用户编号：" + result);
        return result;
    }

    @Override
    public long getUserSeq() {
        return cardDao.getUserSeq();
    }

    @Override
    public Ac01PO getUserByIdCard(String idCard) {
        return cardDao.getUserByIdCard(idCard);
    }

    @Override
    public List<VisualDataDoughunDAO> getVDCollectAC01() {
        return cardDao.getVDCollectAC01();
    }

    @Override
    public Result synchroFromExcel(SynchroExcelVO synchroExcelVO) throws Exception {
        String filePath = "D:\\aaaa.xlsx";
        // 1、读取Excel表
        List<BasicInfoBean> excelBeanList = new ExcelUtil<BasicInfoBean>(new BasicInfoBean()).readFromFile(null, new File(filePath), null);
        // 2、循环遍历,判断
        return null;
    }


    /**
     * 根据采集人员表,组装ac01
     * @param ac01
     * @param bean
     * @throws Exception
     */
    @Override
    public void assembleAC01(Ac01PO ac01, BasicPersonInfoPO bean) throws Exception {
        // 个人编号
        ac01.setAac001(this.generateAC01IUserNumber());
        // 单位编号
        ac01.setAab001(bean.getDepartmentNo().trim());
        // 部门名称
        ac01.setAab004(bean.getDepartmentName().trim());
        // 证件有效期,可能会出现中文 " 长期" 如果出现,则设置为20991231
        if (null != bean.getCertValidity() && "长期".equals(bean.getCertValidity())) {
            ac01.setAab099("20991231");
        } else {
            ac01.setAab099(bean.getCertValidity().trim());
        }
        // 社会保障号=身份证号
        ac01.setAac002(bean.getCertNum().trim());
        // 姓名
        ac01.setAac003(bean.getName().trim());
        // 藏文名字
        ac01.setAac003a(bean.getZangName());
        // 性别
        ac01.setAac004(bean.getSex().trim());
        // 民族
        ac01.setAac005(bean.getNation().trim());
        // 出生年月
        ac01.setAac006(bean.getBirthday().trim());
        // 户口性质
        ac01.setAac009(bean.getHkProperty().trim());
        // 户口所在地
        ac01.setAac010(bean.getAddress().trim());
        // 个人身份
        ac01.setAac012(bean.getPersonStatus().trim());
        // 监护人姓名，年龄小于16周岁的人员，此项必填
        ac01.setAac042(bean.getGuardianName().trim());
        // 监护人证件号码，年龄小于16周岁的人员，此项必填
        ac01.setAac044(bean.getGuardianCertno().trim());

        if (null != bean.getGuardianCertno() && !bean.getGuardianCertno().equals("")) {
            // 监护人证件类型(身份证号码)
            ac01.setAac043(Constants.IDCARD_TYPE_JUMING);
        } else {
            ac01.setAac043("");
        }

        // 证件类型
        ac01.setAac058(Constants.IDCARD_TYPE_JUMING);
        // 生存状态
        ac01.setAac060("1");
        // 电话号码
        ac01.setAac067(bean.getMobile().trim());
        // 身份证号
        ac01.setAac147(bean.getCertNum().trim());
        // 国籍
        ac01.setAac161(bean.getGuoJi().trim());
        // 固定号码
        ac01.setAae005(bean.getPhone().trim());
        // 常住地
        ac01.setAae006(bean.getParmanentAddress().trim());
        // 常住地邮编
        ac01.setAae007(Constants.USUAL_ADDRESS_CODE);
        // 职业(其他)
        ac01.setAca111("");
        // 申领状态
        ac01.setApplyStatus("00");
        // 申领类型 00采集库
        ac01.setApplyType("00");
        // 操作人Id
        ac01.setCreateOperatorId("0");
        // EMAIL
        ac01.setEmail("");
        // 标记状态
        ac01.setMarkStatus("0");
        // 照片路径
        ac01.setPhotourl(bean.getPhotoUrl());
        // 人员状态
        ac01.setStatus(Constants.USER_STATUS_00);
        // 有效标志
        ac01.setValidtag(Constants.USER_VALIDTAG_00);
        // 采集标志
        ac01.setCollectFlag("00");
    }

}
