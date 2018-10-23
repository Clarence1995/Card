package springtestenv;

import com.tecsun.card.common.clarencezeroutils.HttpUtils;
import com.tecsun.card.common.clarencezeroutils.PropertyUtils;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.dao.collect.CollectDao;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.*;
import com.tecsun.card.service.impl.ExceptionTestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 0214
 * @createTime 2018/10/11
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
// @Transactional
public class SpringTest {
    @Autowired
    CollectService           collectService;
    @Autowired
    MidService               midService;
    @Autowired
    CardService              cardService;
    @Autowired
    SystemService            systemService;
    @Autowired
    CardDao                  cardDao;
    @Autowired
    DataHandleService        dataHandleService;
    @Autowired
    CollectDao               collectDao;
    @Autowired
    ExceptionTestServiceImpl exceptionTest;


    @Test
    public void testhandleCollectSynchro() throws Exception {
        String certNum = "511621199501157759";
        // dataHandleService
                // .handleCollectSynchro(certNum, null,false,false,false,false,true, false);
    }


    @Test
    public void testGetUserInfoFromGONGAN() {
        String certNum = "511621199501157759";
        try {
            dataHandleService.getUserInfoFromGongAnByIdCard(certNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteAC01() throws Exception {
        String certNum = "511621199501157759";
        // cardService.deleteAC01ByIdCardAndName(certNum, null);

        // dataHandleService.handleCollectSynchro(certNum, null, false,false,false,false,false,false);
        CollectVO collectVO = new CollectVO();
        collectVO.setCertNum(certNum);
        collectVO.setSynchroStatus("99");
        collectService.updateUserInfoStatusByIdCardAndName(collectVO);
    }
}
