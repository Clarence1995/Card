package springtestenv;

import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.visualdata.UserDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.MidService;
import com.tecsun.card.service.SystemService;
import com.tecsun.card.thread.ThreadPool4j;
import com.tecsun.card.thread.ThreadPool4jImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @RunWith 注释标签是 Junit 提供的，用来说明此测试类的运行者，这里用了 SpringJUnit4ClassRunner，
 * 这个类是一个针对 Junit 运行环境的自定义扩展，用来标准化在 Spring 环境中 Junit4.5 的测试用例，例如支持的注释标签的标准化
 * @ContextConfiguration 注释标签是 Spring test context 提供的，用来指定 Spring 配置信息的来源，支持指定 XML 文件位置或者 Spring 配置类名，这里我们指定 classpath 下的 /config/Spring-db1.xml 为配置文件的位置
 * @Transactional 注释标签是表明此测试类的事务启用，这样所有的测试方案都会自动的 rollback，即您不用自己清除自己所做的任何对数据库的变更了
 * @Autowired 体现了我们的测试类也是在 Spring 的容器中管理的，他可以获取容器的 bean 的注入，您不用自己手工获取要测试的 bean 实例了
 * testGetAccountById 是我们的测试用例：注意和上面的 AccountServiceOldTest 中相同的测试方法的对比，这里我们不用再 try-catch-finally 了，事务管理自动运行，当我们执行完成后，所有相关变更会被自动清除
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
@Transactional
public class SpirngMybatisTest {
    @Autowired
    CollectService collectService;
    @Autowired
    MidService midService;
    @Autowired
    CardService cardService;
    @Autowired
    SystemService systemService;
    @Autowired
    CardDao cardDao;


    @Test
    public void testTable() {
        UserDAO list  =  cardDao.getUserTable("SISP_CARD");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", list);
        System.out.println(jsonObject.toJSONString());


        // 1、获取列字段注释
//        String columnComments = cardDao.getColumnCommonByColumnName("T_BANK", "BANK_ID");
//        System.out.println(columnComments);
//
//        List<ColumnDAO> list = cardDao.getTableColumn("AC01");
//        for (ColumnDAO columnDAO : list) {
//            System.out.println(columnDAO);
//        }
//
    }

    @Test
    public void RedisDicTest() {
        Result res = systemService.initRedis();
        System.out.println(res);
    }

    @Test
    public void collectVDTest () {
        List<VisualDataDoughunDAO> result = midService.getVDCollectAC01();
        for (VisualDataDoughunDAO visualDataDoughunDAO : result) {
            System.out.println(visualDataDoughunDAO);
        }
    }

    @Test
    public void visualDataTest () {
        List<VisualDataDoughunDAO> list = collectService.getVDBasicPersonAnalyse();
        for (VisualDataDoughunDAO visualDataDoughunDAO : list) {
            System.out.println(visualDataDoughunDAO);
        }
    }

    @Test
    public void threadInitTest () {
        ThreadPool4j threadPool = new ThreadPool4jImpl();
        threadPool.init();
    }

    @Test
    public void getFiles () {
        String strPath = "E:\\tecsun\\photo\\synchro";
        List<String> result = new ArrayList<>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    continue;
                } else if (fileName.endsWith(".jpg")) {
                    result.add(fileName.substring(0, fileName.lastIndexOf(".")));
                }
            }

        }
        for (String s : result) {
            System.out.println("fileName: " + s);
        }
    }

    @Test
    public void testValidate () {
        BasicPersonInfoPO bean = new BasicPersonInfoPO();
        bean.setCertNum("542626198012010047");
        bean.setRegionalCode("510100");
        bean.setGuoJi("a");
        bean.setName("b");
        bean.setNation("c");
        bean.setAddress("d");
        bean.setCertType("e");
        bean.setCertValidity("f");
        bean.setMobile("g");
        bean.setSex("0");
        bean.setParmanentAddress("e");
        System.out.println(collectService.validateBasicPersonInfo(bean));
        System.out.println(bean.getDealMsg());
    }

    @Test
    public void updateBasicInfo () {
        CollectVO collectVO = new CollectVO();
        collectVO.setCertNum("542626198012010047");
        collectVO.setSynchroStatus(Constants.COLLECT_HAD_SYNCHRO);
        collectVO.setDealStaus(Constants.COLLECT_QUALIFIED);
        collectService.updateBasicPersonInfoStatus(collectVO);
    }

    @Test
    public void insertBusApply () {
        BusApplyPO busApplyPO = new BusApplyPO();
        busApplyPO.setBusinessType(Constants.BUSINESS_TYPE_01); // 01 新申领
        busApplyPO.setStatus(Constants.APPLY_STATUS_00);        // 00 申领状态: 申请
        busApplyPO.setSource("00");              // 01 个人申领
        busApplyPO.setRegionalId("510100"); // 区域编码
        busApplyPO.setApplyName("a");
        busApplyPO.setApplyIdCard("511621199501157759");
        busApplyPO.setApplyMobile("18813295493");
        busApplyPO.setFlag("00");
        busApplyPO.setChooseCardNo("");
        busApplyPO.setChangeBankNo("00");
        cardService.insertBusApply(busApplyPO);
    }

    @Test
    public void CardDaoTest () {
        String idCard = "511621199501157759";
        boolean result = cardService.userExistInCard(idCard);
        System.out.println(result);
    }


    @Test
    public void mybatisListTest () {
        CollectVO collectVO = new CollectVO();
        collectVO.setCertNum("511621199501157759");
        collectVO.setSynchroStatus("0");
        List<BasicPersonInfoPO> listBean = collectService.listQualifiedBasicPerson(collectVO);
        for (BasicPersonInfoPO basicPersonInfoPO : listBean) {
            System.out.println(basicPersonInfoPO.getCertNum());
        }
    }

}
