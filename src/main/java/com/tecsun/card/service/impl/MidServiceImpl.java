package com.tecsun.card.service.impl;

import com.tecsun.card.dao.mid.MidDao;
import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.service.MidService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("midService")
public class MidServiceImpl implements MidService {
    @Resource
    private MidDao midDao;
    @Override
    public MidImgDAO getImgFromGONGAN (String idCard) {
        return midDao.getImgFromGONGAN(idCard);
    }

    @Override
    public MidImgDAO getImgFromGAT12 (String idCard) {
        return midDao.getImgFromGAT12(idCard);
    }

    @Override
    public MidImgDAO getImgFromCOLLECTPHOTO (String idCard) {
        return midDao.getImgFromCOLLECTPHOTO(idCard);
    }

    @Override
    public List<VisualDataDoughunDAO> getVDCollectAC01 () {
        return midDao.getVDCollectAC01();
    }

    @Override
    public BasicPersonInfo getBasicInfoByIdCard(String idCard) {
        return midDao.getBasicPersonByIdCardInMid(idCard);
    }
}
