package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class QueryParams implements Serializable {

    private Integer OrgId;
    private Integer PageIndex;
    private Integer PageSize;

    public Integer getOrgId() {
        return OrgId;
    }

    public void setOrgId(Integer orgId) {
        OrgId = orgId;
    }

    public Integer getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        PageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return PageSize;
    }

    public void setPageSize(Integer pageSize) {
        PageSize = pageSize;
    }
}
