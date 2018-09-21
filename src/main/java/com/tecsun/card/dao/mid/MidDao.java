package com.tecsun.card.dao.mid;

import com.tecsun.card.entity.beandao.mid.MidImgDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import org.apache.ibatis.annotations.Param;import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface MidDao {
    MidImgDAO getImgFromGONGAN(@Param("idCard")String idCard);

    MidImgDAO getImgFromGAT12(@Param("idCard")String idCard);

    MidImgDAO getImgFromCOLLECTPHOTO(@Param("idCard")String idCard);

    List<VisualDataDoughunDAO> getVDCollectAC01 ();


    /**
     * 临时接口
     * @param idCard
     * @return
     */
    BasicPersonInfoPO getBasicPersonByIdCardInMid(@Param("idCard")String idCard);
}
