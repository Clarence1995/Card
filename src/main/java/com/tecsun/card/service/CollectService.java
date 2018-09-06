package com.tecsun.card.service;

import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.vo.CollectVO;

import java.util.List;

public interface CollectService {
    List<BasicPersonInfoPO> listAllBasicPersonInfoPO();

    int updateBasicPersonInfoStatus (CollectVO CollectVO);

    List<BasicPersonInfoPO> listQualifiedBasicPerson (CollectVO synchroStatus);

    String getZangName (String idCard);

    boolean validateBasicPersonInfo(BasicPersonInfoPO basicBean);

    List<BasicPersonInfoPO> getBasicInfoFromList (List<String> idCardList);

    List<VisualDataDoughunDAO> getVDBasicPersonAnalyse ();

    BasicPersonInfoPO getBasicInfoByIDCard(String idCard);
}
