package com.tecsun.card.dao.mid;

import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface MidDao {
    MidImgDAO getImgFromGONGAN (String idCard);

    MidImgDAO getImgFromGAT12 (String idCard);

    MidImgDAO getImgFromCOLLECTPHOTO (String idCard);

    List<VisualDataDoughunDAO> getVDCollectAC01 ();
}
