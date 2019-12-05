package com.acm.pojo.vo;


public class PermissionVO {
    private Integer pId;

    private String pUrl;

    private String pName;

    private String pCode;

    private Integer pageNum;

    private Integer pageSize;

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

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
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

    @Override
    public String toString() {
        return "UserVO{" +
                "pId=" + pId +
                ", pUrl='" + pUrl + '\'' +
                ", pName=" + pName +
                ", pcode=" + pCode +
                '}';
    }
}
