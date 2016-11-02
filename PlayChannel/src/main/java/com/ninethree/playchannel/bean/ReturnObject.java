package com.ninethree.playchannel.bean;

import java.io.Serializable;

/**
 * Created by user on 2016/8/2.
 */
public class ReturnObject implements Serializable {

    private  Integer UserId;
    private  String UserGuid;

    private User UserBasic;
    private Org Org;

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public String getUserGuid() {
        return UserGuid;
    }

    public void setUserGuid(String userGuid) {
        UserGuid = userGuid;
    }

    public User getUserBasic() {
        return UserBasic;
    }

    public void setUserBasic(User userBasic) {
        UserBasic = userBasic;
    }

    public com.ninethree.playchannel.bean.Org getOrg() {
        return Org;
    }

    public void setOrg(com.ninethree.playchannel.bean.Org org) {
        Org = org;
    }
}
