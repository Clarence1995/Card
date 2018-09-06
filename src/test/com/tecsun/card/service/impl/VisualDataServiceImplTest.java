package com.tecsun.card.service.impl;

import com.tecsun.card.service.VisualDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
@Transactional
public class VisualDataServiceImplTest {

    @Autowired
    VisualDataService visualDataService;
    @Test
    public void getVisualDataFromCollectAndCard () {
    }

    @Test
    public void getTableCollectBasic () {
        // System.out.println(redisAspect);
    }

    @Test
    public void getTableCollectInfo () {
    }

    @Test
    public void getTableCardUserInfo () {
    }
}