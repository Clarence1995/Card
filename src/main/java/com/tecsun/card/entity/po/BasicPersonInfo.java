package com.tecsun.card.entity.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class BasicPersonInfo {
    private Long id;
    /**
     * 户籍所在地
     */
    private String address;
    private String addressCode;
    private String adultFlag;
    /**
     * 出生日期
     */
    private String birthday;
    private String certType;
    private String certNum;
    /**
     * 证件有效期
     */
    private String certValidity;
    private String departmentNo;
    private String departmentName;
    private String education;
    private String expressName;
    private String expressPhone;
    private String expressAddress;
    private String guardianName;
    private String guardianCertno;
    private String guardianContact;

    private String hkType;
    private String hkbNo;
    /**
     * 户口性质
     */
    private String hkProperty;
    private String isExpress;
    private String mobile;
    private String name;
    /**
     * 民族
     */
    private String nation;
    private String parmanentAddress;
    private String parmanentZipCode;
    private String parmanentAddresscode;
    /**
     * 个人身份
     */
    private String personStatus;
    /**
     * 固定电话
     */
    private String phone;
    /**
     * 性别
     */
    private Integer sex;
    private String regionalCode;



    private String photoUrl;
    private String isMarry;
    private String bankCode;
    private String photoSource;
    private String gongAnStatus;
    private String guoJi;
    private String familyNo;
    private String dealstatus;
    private String personno;
    private String createTime;
    private String synchroTime;
    private String deviceId;
    private String certUp;
    private String certDown;
    private String hkbUrl;
    private String cbplace;
    private String fkplace;

    private String bussType;
    private String politicalStatus;
    private String topBankName;
    private String isBaby;
    private String gis;
    private String zangName;
    private String synchroStatus;

    private String insuredSituation;
    private String noinsuredreason;
    private String jobSituation;
    private String personInfo;
    private String reviewStatus;
    private String aac001;
    private String ryzt;
    private String dbStatus;
    private String aac008;
    private String medicalStatus;
    private String noJobReason;
    private String zangWenStatus;
    private String gonganStatus;

    private String isEnsure;
    private String updateTime;
    private String dealMsg;
    private String declarephotoUrl;
    private String declareNo;
    private String bankDeclareStatus;
    private String signphotoUrl;
}