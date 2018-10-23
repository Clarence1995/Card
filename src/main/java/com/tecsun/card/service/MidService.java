package com.tecsun.card.service;

import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfo;

import java.util.List;

public interface MidService {
    MidImgDAO getImgFromGONGAN (String idCard);

    MidImgDAO getImgFromGAT12 (String idCard);

    MidImgDAO getImgFromCOLLECTPHOTO (String idCard);

    List<VisualDataDoughunDAO> getVDCollectAC01 ();

    BasicPersonInfo getBasicInfoByIdCard(String idCard);
}
