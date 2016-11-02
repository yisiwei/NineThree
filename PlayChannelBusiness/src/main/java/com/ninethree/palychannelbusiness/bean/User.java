package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -3984728208644907150L;

    private String UserID;
    private String UserGuid;
    private String UserNum;
    private String UserName;
    private String NickName;
    private String RealName;
    private Integer RealNameChecked;
    private String Email;
    private Integer EmailChecked;
    private String Phone;
    private String Mobile;
    private Integer MobileChecked;
    private Integer Sex;
    private String Photo;
    private String MyTime;
    // private Integer BnAccepFlag;
    private String BnAcceptFlag;
    private String DefaultOrgID;
    private String DefaultOrgGuid;
    private String SeftOrgID;
    private String SeftOrgGuid;
    private String SeftOrgName;
    private String RegisterIP;
    private Integer BeSearchState;
    private String Birthday;
    private String Province;
    private String City;
    private String County;
    private String Address;
    private String Postcode;
    private String EduRecordId;
    private String EduRecordChecked;
    private String Flag;

    private String OrgId;
    private String OrgGuid;

    private String UsedTime;
    private String AddTime;

    private String BalancePar;

    public User() {

    }

    public User(String nickName) {
        super();
        NickName = nickName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserGuid() {
        return UserGuid;
    }

    public void setUserGuid(String userGuid) {
        UserGuid = userGuid;
    }

    public String getUserNum() {
        return UserNum;
    }

    public void setUserNum(String userNum) {
        UserNum = userNum;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public Integer getRealNameChecked() {
        return RealNameChecked;
    }

    public void setRealNameChecked(Integer realNameChecked) {
        RealNameChecked = realNameChecked;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getEmailChecked() {
        return EmailChecked;
    }

    public void setEmailChecked(Integer emailChecked) {
        EmailChecked = emailChecked;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public Integer getMobileChecked() {
        return MobileChecked;
    }

    public void setMobileChecked(Integer mobileChecked) {
        MobileChecked = mobileChecked;
    }

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getMyTime() {
        return MyTime;
    }

    public void setMyTime(String myTime) {
        MyTime = myTime;
    }

    public String getDefaultOrgID() {
        return DefaultOrgID;
    }

    public void setDefaultOrgID(String defaultOrgID) {
        DefaultOrgID = defaultOrgID;
    }

    public String getDefaultOrgGuid() {
        return DefaultOrgGuid;
    }

    public void setDefaultOrgGuid(String defaultOrgGuid) {
        DefaultOrgGuid = defaultOrgGuid;
    }

    public String getSeftOrgID() {
        return SeftOrgID;
    }

    public void setSeftOrgID(String seftOrgID) {
        SeftOrgID = seftOrgID;
    }

    public String getSeftOrgGuid() {
        return SeftOrgGuid;
    }

    public void setSeftOrgGuid(String seftOrgGuid) {
        SeftOrgGuid = seftOrgGuid;
    }

    public String getSeftOrgName() {
        return SeftOrgName;
    }

    public void setSeftOrgName(String seftOrgName) {
        SeftOrgName = seftOrgName;
    }

    public String getRegisterIP() {
        return RegisterIP;
    }

    public void setRegisterIP(String registerIP) {
        RegisterIP = registerIP;
    }

    public Integer getBeSearchState() {
        return BeSearchState;
    }

    public void setBeSearchState(Integer beSearchState) {
        BeSearchState = beSearchState;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public String getEduRecordId() {
        return EduRecordId;
    }

    public void setEduRecordId(String eduRecordId) {
        EduRecordId = eduRecordId;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getBnAcceptFlag() {
        return BnAcceptFlag;
    }

    public void setBnAcceptFlag(String bnAcceptFlag) {
        BnAcceptFlag = bnAcceptFlag;
    }

    public String getEduRecordChecked() {
        return EduRecordChecked;
    }

    public void setEduRecordChecked(String eduRecordChecked) {
        EduRecordChecked = eduRecordChecked;
    }

    public String getOrgId() {
        return OrgId;
    }

    public void setOrgId(String orgId) {
        OrgId = orgId;
    }

    public String getOrgGuid() {
        return OrgGuid;
    }

    public void setOrgGuid(String orgGuid) {
        OrgGuid = orgGuid;
    }

    public String getUsedTime() {
        return UsedTime;
    }

    public void setUsedTime(String usedTime) {
        UsedTime = usedTime;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getBalancePar() {
        return BalancePar;
    }

    public void setBalancePar(String balancePar) {
        BalancePar = balancePar;
    }

    @Override
    public String toString() {
        return "User [UserID=" + UserID + ", UserGuid=" + UserGuid
                + ", UserNum=" + UserNum + ", UserName=" + UserName
                + ", NickName=" + NickName + ", RealName=" + RealName
                + ", RealNameChecked=" + RealNameChecked + ", Email=" + Email
                + ", EmailChecked=" + EmailChecked + ", Phone=" + Phone
                + ", Mobile=" + Mobile + ", MobileChecked=" + MobileChecked
                + ", Sex=" + Sex + ", Photo=" + Photo + ", MyTime=" + MyTime
                + ", BnAcceptFlag=" + BnAcceptFlag + ", DefaultOrgID="
                + DefaultOrgID + ", DefaultOrgGuid=" + DefaultOrgGuid
                + ", SeftOrgID=" + SeftOrgID + ", SeftOrgGuid=" + SeftOrgGuid
                + ", SeftOrgName=" + SeftOrgName + ", RegisterIP=" + RegisterIP
                + ", BeSearchState=" + BeSearchState + ", Birthday=" + Birthday
                + ", Province=" + Province + ", City=" + City + ", County="
                + County + ", Address=" + Address + ", Postcode=" + Postcode
                + ", EduRecordId=" + EduRecordId + ", EduRecordChecked="
                + EduRecordChecked + ", Flag=" + Flag + "]";
    }

}
