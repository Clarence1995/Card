package com.tecsun.card.entity.vo;

/**
 * Created by yangliu on 2017/7/8.
 * 综合服务  出参
 */
public class CardServiceVO {

    private int type;//服务类型    1：激活   2：临时挂失   3：正式挂失：   4：解挂  5：注销
    private long  userId;//人员id
    private long cardId;//卡id

    private String AAC001;//个人编号
    private String AAC002;//社会保障号码
    private String AAC003;//姓名
    private String AAC003A;//藏名
    private String aac004; //性别
    private String aac005;//民族
    private String cardNo;//卡号
    private String status;//人员状态
    private String cardAppStatus;//卡应用状态
    private String cardStatus;//卡状态
    private String AAC147;//身份证号码
    private String AAZ501;//卡识别码
    private String validtag;
    private String gsTime;//挂失时间
    private String aac058;//证件类型
    private String aae053;

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getAae053() {
        return aae053;
    }

    public void setAae053(String aae053) {
        this.aae053 = aae053;
    }

    public String getAac058() {
        return aac058;
    }

    public void setAac058(String aac058) {
        this.aac058 = aac058;
    }

    public String getGsTime() {
        return gsTime;
    }

    public void setGsTime(String gsTime) {
        this.gsTime = gsTime;
    }

    public String getValidtag() {
        return validtag;
    }

    public void setValidtag(String validtag) {
        this.validtag = validtag;
    }

    public String getAAC147() {
        return AAC147;
    }

    public void setAAC147(String AAC147) {
        this.AAC147 = AAC147;
    }

    public String getAAZ501() {
        return AAZ501;
    }

    public void setAAZ501(String AAZ501) {
        this.AAZ501 = AAZ501;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCardAppStatus(String cardAppStatus) {
        this.cardAppStatus = cardAppStatus;
    }

    public String getAac005() {
        return aac005;
    }

    public void setAac005(String aac005) {
        this.aac005 = aac005;
    }

    public String getAAC003A() {
        return AAC003A;
    }

    public void setAAC003A(String AAC003A) {
        this.AAC003A = AAC003A;
    }

    public String getAac004() {
        return aac004;
    }

    public void setAac004(String aac004) {
        this.aac004 = aac004;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getAAC001() {
        return AAC001;
    }

    public void setAAC001(String AAC001) {
        this.AAC001 = AAC001;
    }

    public String getAAC002() {
        return AAC002;
    }

    public void setAAC002(String AAC002) {
        this.AAC002 = AAC002;
    }

    public String getAAC003() {
        return AAC003;
    }

    public void setAAC003(String AAC003) {
        this.AAC003 = AAC003;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getStatus() {
        return status;
    }

    public String getCardAppStatus() {
        return cardAppStatus;
    }
}
