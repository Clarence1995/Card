package com.tecsun.card.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.tecsun.card.common.excel.ExcelUtil;
import com.tecsun.card.common.txt.TxtUtil;
import com.tecsun.card.controller.BatchBean;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.entity.BasicInfoBean;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.AZ01DAO;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.card.SysLogDAO;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DealBatchService {
    private final Logger      logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private       CardDao     cardDao;
    @Autowired
    private       CardService cardService;

    @Transactional(value = "springJTATransactionManager", rollbackFor = Exception.class)
    public Result dealBatch(String filePath) throws Exception {
        Result          result    = new Result();
        List<BatchBean> beanList  = new ExcelUtil<BatchBean>(new BatchBean()).readFromFile(null, new File(filePath), null);
        List<String>    errorList = new ArrayList<>();
        // 2、数据比对
        boolean valifyResult = true;
        for (BatchBean batchBean : beanList) {
            StringBuilder str    = new StringBuilder();
            String        idCard = batchBean.getAAC147();
            str.append("用户_" + idCard + "_" + batchBean.getAAC003());
            // 查询数据库信息
            BatchBean       dataBean     = null;
            List<BatchBean> dataBeanList = cardDao.getBatchBeanByIdCard(idCard);
            if (dataBeanList.size() > 1) {
                // 记录数据
                str.append("_卡管数据库存在多条记录");
                errorList.add(str.toString());
                continue;
            } else if (null == dataBeanList || dataBeanList.size() == 0) {
                str.append("_卡管数据库查无此人");
                errorList.add(str.toString());
                continue;
            }
            dataBean = dataBeanList.get(0);
            if (!batchBean.getAAC001().equals(dataBean.getAAC001())) {
                str.append("_个人编码不等" + "Excel(" + batchBean.getAAC001() + "),Database(" + dataBean.getAAC001() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAZ500().equals(dataBean.getAAZ500())) {
                str.append("_卡号不等" + "Excel(" + batchBean.getAAZ500() + "),Database(" + dataBean.getAAZ500() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAC003().equals(dataBean.getAAC003())) {
                str.append("_姓名不等" + "Excel(" + batchBean.getAAC003() + "),Database(" + dataBean.getAAC003() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAC004().equals(dataBean.getAAC004())) {
                str.append("_性别码不等" + "Excel(" + batchBean.getAAC004() + "),Database(" + dataBean.getAAC004() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAC006().equals(dataBean.getAAC006())) {
                str.append("_出生日期不等" + "Excel(" + batchBean.getAAC006() + "),Database(" + dataBean.getAAC006() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAE053().equals(dataBean.getAAE053())) {
                str.append("_银行卡号不等" + "Excel(" + batchBean.getAAE053() + "),Database(" + dataBean.getAAE053() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAC067().equals(dataBean.getAAC067())) {
                str.append("_联系电话不等" + "Excel(" + batchBean.getAAC067() + "),Database(" + dataBean.getAAC067() + ")");
                valifyResult = false;
            }
            if (!batchBean.getAAZ501().substring(0, 24).equals(dataBean.getAAZ501())) {
                str.append("_24位卡识别码不等" + "Excel(" + batchBean.getAAZ501() + "),Database(" + dataBean.getAAZ501() + ")");
                valifyResult = false;
            }

            if (!valifyResult) {
                errorList.add(str.toString());
                continue;
            }

            // 更新数据库
            logger.info("此人{} 同步成功", idCard);

        }
        TxtUtil.writeTxt(new File("E:\\logs\\errors.txt"), "UTF-8", errorList);
        logger.info("这一批同步人数为: {}, 信息不全的人有: {},集合: {}", beanList.size(), errorList.size(), errorList.toString());
        result.setStateCode(200);
        result.setMsg("同步成功");
        result.setData(errorList);
        return result;
    }

    /**
     * // 1、读取Excel
     * // 2、判断数据库人员是否存在，不存在，日志记录
     * // 3、人员正式挂失（判断是否有银行卡账号，若有，则需要调银行挂失接口）
     * // 4、新增一条补换申领操作
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    @Transactional(value = "springJTATransactionManager", rollbackFor = Exception.class)
    public Result dealBHSL(String filePath){
        Result result = new Result();
        List<String> errorList = new ArrayList<>();
        try {
            // 1、读取Excel
            List<BasicInfoBean> excelBeanList = new ExcelUtil<BasicInfoBean>(new BasicInfoBean()).readFromFile(null, new File(filePath), null);
            logger.info("获取Excel表人数为: {}", excelBeanList.size());
            // 错误日志记录List
            int          success   = 0;
            int          fail      = 0;
            for (BasicInfoBean user : excelBeanList) {
                String idCard = user.getAAC147();
                String name   = user.getAAC003();
                // 2、获取数据库人员详细信息
                List<Ac01DAO> userExitList = cardDao.getUserByIdCardAndName(idCard, name);
                if (userExitList == null || userExitList.size() == 0) {
                    logger.info("用户: {} 在数据库中不存在", idCard);
                    errorList.add(idCard + "_" + name + "_在数据库中不存在此人");
                    fail++;
                    continue;
                }
                Ac01DAO userFromDB = userExitList.get(0);
                String  AAE053     = userFromDB.getAAE053();
                user.setAAC058(userFromDB.getAAC058());
                if (null != AAE053 && !("").equals(AAE053)) {
                    // 有银行卡账号，则需要调用银行挂失接口
//                    boolean bankResult = notifyBank(userFromDB);
//                    if (bankResult) {
//                        logger.info("用户： {}_调用银行接口挂失成功", idCard);
//                    } else {
//                        errorList.add(idCard + "_" + name + "_调用银行接口失败");
//                        logger.info("用户： {}_调用银行接口挂失失败", idCard);
//                        fail++;
//                        continue;
//                    }
                    // 正式挂失 人员状态status: 05（正式挂失） 卡应用状态 AAZ502： 2(正式挂失） 申领状态Apply_status 00  时间 gsTime
                    SimpleDateFormat format  = new SimpleDateFormat("yyyyMMdd");
                    String           nowDate = format.format(new Date());
                    // 更新人员状态
                    Ac01DAO ac01DAO = new Ac01DAO();
                    ac01DAO.setUserStatus("00");
                    ac01DAO.setApplyStatus("00");
                    ac01DAO.setAAC147(idCard);
                    int userUpdateResult = cardDao.updateAC01Status(ac01DAO);
                    // 更新卡应用状态为注销状态
                    AZ01DAO az01DAO = new AZ01DAO();
                    az01DAO.setCardId(userFromDB.getCardId());
                    az01DAO.setCardApplyStatus("9");
                    az01DAO.setGsTime(nowDate);
                    int cardUpdateResult = cardDao.updateAZ01StatusByIdCardAndName(az01DAO);

                    // 日志记录
                    String sysLogContent =
                            "用户ID为" + userFromDB.getId() +
                                    " 卡ID为" + userFromDB.getCardId() +
                                    "的用户在 " + nowDate + " 申请了 正式挂失 操作！（操作时用户状态为：" + "05 ;卡应用状态为:" + az01DAO.getCardApplyStatus() + "）";
                    SysLogDAO sysLogDAO = new SysLogDAO();
                    sysLogDAO.setContent(sysLogContent);
                    sysLogDAO.setBussinessType("06");
                    sysLogDAO.setUserId("1");
                    cardDao.insertSysLog(sysLogDAO);
                    // 补换申领 之前的卡应用状态： AAZ502: 9(注销）
                    // AC01 APPLY_TYPE: 01： 个人申领渠道 APPLY_TYPE: 00
                    // 申领表
                    // 日志记录

                    //生成申领表编码,规则：账号+区域id+10位流水号（8位年月日+2位随机数）
                    BusApplyPO busApplyPO = new BusApplyPO();
                    // 普通换卡
                    busApplyPO.setBusinessType(Constants.BUSINESS_TYPE_06);
                    // 00 申请
                    busApplyPO.setStatus(Constants.APPLY_STATUS_00);
                    // 个人申领
                    busApplyPO.setSource("01");
                    // 区域编码
                    busApplyPO.setRegionalId(userFromDB.getAAC301());
                    busApplyPO.setApplyName(userFromDB.getAAC003());
                    busApplyPO.setApplyIdCard(userFromDB.getAAC147());
                    busApplyPO.setApplyMobile(userFromDB.getAAC067());
                    busApplyPO.setFlag("00");
                    busApplyPO.setChooseCardNo("");
                    busApplyPO.setChangeBankNo("00");
                    busApplyPO.setBhyy("人员信息出错");
                    busApplyPO.setIsexpress("00");
                    busApplyPO.setPersonId(userFromDB.getId());
                    // busApplyPO.setApplyFormCode("admin" + userFromDB.getAAC301() + nowDate + (new Random().nextInt(90) + 10));
                    // 插入新申领表
                    cardService.insertBusApply(busApplyPO);

//                    // 回调西藏银行接口
//                    JSONObject json = new JSONObject();
//                    //要素名称	英文名称
//                    //姓名	custName
//                    //银行卡号	CardNo
//                    //证件类型	IDType
//                    //证件号码	IDNo
//                    //社保保障号码	sbCardNo
//                    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
//                    String           date    = format2.format(new Date());
//                    SimpleDateFormat format3 = new SimpleDateFormat("HHmmss");
//                    String           time    = format3.format(new Date());
//                    SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//                    String           sn      = format4.format(new Date());
//                    json.put("Version", "1.0.0");
//                    json.put("ChannelDate", date);
//                    json.put("ChannelTm", time);
//                    json.put("ChannelSN", sn);
//                    json.put("Branch", "tecsun");
//                    json.put("TellerNo", userFromDB.getAAC003());
//                    json.put("TellerPhone", userFromDB.getAAC067());
//                    json.put("custName", userFromDB.getAAC003());
//                    json.put("CardNo", userFromDB.getAAE053());
//                    json.put("IDType", userFromDB.getAAC058());
//                    json.put("IDNo", userFromDB.getAAC147());
//                    json.put("TransCode", "IBS2005");
//                    json.put("sbCardNo", userFromDB.getAAZ500());
//                    logger.info("访问西藏银行参数:" + json);
//                    String TECSUN_XZ_URL = "http://133.33.8.16:8700";
//                    try {
//                        String wsResult6 = (String) getWebClient(TECSUN_XZ_URL, json, String.class);
//                        logger.info("回调西藏银行的申领通知接口 返回结果：" + wsResult6);
//                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(wsResult6);
//                        String                          statusCode = (String) jsonObject.get("RetCode");
//                        String                          msg        = (String) jsonObject.get("RetMsg");
//                        if (!statusCode.equals("00")) {
//                            logger.info("银行返回错误信息为：" + msg);
//                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                            errorList.add("idCard:" + idCard + "_" + "西藏银行申领补换调用出错");
//                            fail++;
//                            continue;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        errorList.add("用户: " + idCard + "银行调用出错:" + e.getMessage() + "数据库回滚");
//                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                    }
                }else{
                    // 没有银行卡账号的,日志记录
                    logger.info("用户： {} 没有银行卡账号");
                    errorList.add("用户： " + idCard + "_没有银行卡账号");
                    fail++;
                    continue;
                }
                success++;
            }
            result.setMsg("所有数据处理完成");
            result.setData("成功人数： " + success + " 失败人数： " + fail);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("数据处理发生异常，正在数据回滚");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            try {
                TxtUtil.writeTxt(new File("D:\\synchroClarencezero\\error.txt"), "UTF-8", errorList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean notifyBank(Ac01DAO applyTemVO) {
        String           msg     = "";
        SimpleDateFormat format  = new SimpleDateFormat("yyyyMMdd");
        String           date    = format.format(new Date());
        SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
        String           time    = format2.format(new Date());
        SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String           sn      = format3.format(new Date());
        JSONObject       json    = new JSONObject();
        json.put("Version", "1.0.0");
        json.put("ChannelDate", date);
        json.put("ChannelTm", time);
        json.put("ChannelSN", sn);
        json.put("Branch", "tecsun");
        // 姓名
        json.put("TellerNo", applyTemVO.getAAC003());
        // 手机号码
        json.put("TellerPhone", applyTemVO.getAAC067());
        // 姓名
        json.put("custName", applyTemVO.getAAC003());
        // 银行卡号
        json.put("CardNo", applyTemVO.getAAE053());
        // 证件类型 01 居民身份证
        json.put("IDType", applyTemVO.getAAC058());
        // 身份证号
        json.put("IDNo", applyTemVO.getAAC147());
        try {
            json.put("TransCode", "IBS2002");
            json.put("sbCardNo", applyTemVO.getAAE053());
            json.put("LossRegType", 3);
            logger.info("访问西藏银行入参为：{}", json.toJSONString());
            String TECSUN_XZ_URL = "http://133.33.8.16:8700";
            String wsResult6     = (String) getWebClient(TECSUN_XZ_URL, json, String.class);
            logger.info("回调西藏银行的挂失通知接口 返回结果：{}", wsResult6);
            JSONObject jsonObject = JSON.parseObject(wsResult6);
            String     statusCode = (String) jsonObject.get("RetCode");
            msg = (String) jsonObject.get("RetMsg");
            if ("00".equals(statusCode)) {
                return true;
            }
            logger.error("银行返回错误信息为：{}", msg);
            return false;
        } catch (Exception e) {
            if (null != e.getCause()) {
                msg = e.getCause().getMessage();
            } else {
                msg = e.getMessage();
            }
            logger.error("回调西藏银行的补换卡通知接口 异常: {}", msg);
            return false;
        }
    }

    public static Object getWebClient(String url, JSONObject json, Class resultClass) {
        ClientConfig cc          = new DefaultClientConfig();
        Client       client      = Client.create(cc);
        WebResource  webResource = client.resource(url);
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, json.toString());
        client.destroy();
        return response.getEntity(resultClass);
    }

    public void writeTxtByTemplate() {

    }
}
