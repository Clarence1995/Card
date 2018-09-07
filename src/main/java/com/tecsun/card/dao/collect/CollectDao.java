package com.tecsun.card.dao.collect;

import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface CollectDao {
    List<BasicPersonInfoPO> lisetBasicPersonInfo();

    int updateBasicPersonInfoStatus (BasicPersonInfoDAO basicDao);

    List<BasicPersonInfoPO> listQualifiedBasicPerson (BasicPersonInfoDAO basicPersonInfoDAO);

    String getZangName (String idCard);

    List<BasicPersonInfoPO> listBasicBeanByIdList (List<String> idCardList);

    List<VisualDataDoughunDAO> listVDBasicPersonAnalyset ();

    BasicPersonInfoPO getBasicPersonByIdCard(@Param("idCard")String idCard);
}
