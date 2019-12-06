package com.acm.pojo.vo;


public class PermissionVO {
    private Integer pId;

    private String pUrl;

    private String pName;

    private String pType;

    private Integer pageNum;

    private Integer pageSize;

    private String token;

    /**
     * 获取主键
     *
     * @return p_id - 主键
     */
    public Integer getpId() {
        return pId;
    }

    /**
     * 设置主键
     *
     * @param pId 主键
     */
    public void setpId(Integer pId) {
        this.pId = pId;
    }

    /**
     * @return p_url
     */
    public String getpUrl() {
        return pUrl;
    }

    /**
     * @param pUrl
     */
    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    /**
     * @return p_name
     */
    public String getpName() {
        return pName;
    }



    /**
     * @param pName
     */
    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "pId=" + pId +
                ", pUrl='" + pUrl + '\'' +
                ", pName=" + pName +
                ", pType=" + pType +
                '}';
    }
}
