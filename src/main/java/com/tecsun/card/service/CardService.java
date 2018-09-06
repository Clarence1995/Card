package com.tecsun.card.service;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.SynchroExcelVO;

import java.util.List;

public interface CardService {
    List<Ac01PO> listAllAc01();

    int updateAC01Status (Ac01DAO acstatusBean);


    boolean userExistInCard (String idCard);

    boolean insertAC01(Ac01PO ac01PO);

    void assembleAC01(Ac01PO ac01PO, BasicPersonInfoPO bean) throws Exception;


    boolean insertCardAC01AndBusApplyFromCollect (Ac01PO ac01bean, BasicPersonInfoPO basicPersonInfoPO);

    boolean insertBusApply(BusApplyPO busApplyPO);

    String generateAC01IUserNumber() throws Exception;

    long getUserSeq();

    Ac01PO getUserByIdCard (String idCard);

    List<VisualDataDoughunDAO> getVDCollectAC01 ();

    Result synchroFromExcel(SynchroExcelVO synchroExcelVO) throws Exception;
}
