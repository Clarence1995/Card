package com.tecsun.card.service.impl;

import com.tecsun.card.dao.pub.PubDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
@Transactional
public class PublicServiceTest {
    @Autowired
    private PubDao pubDao;
    @Test
    public void testJudgeRegionalcode() {
        String regionalcode = "542121143333";
        // boolean result =  pubDao.countRegionalCode(regionalcode);
        // System.out.println(result);
    }
}