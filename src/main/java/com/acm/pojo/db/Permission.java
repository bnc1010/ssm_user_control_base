package com.acm.pojo.db;

import javax.persistence.*;

public class Permission {
    /**
     * 主键
     */
    @Id
    @Column(name = "p_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pId;

    @Column(name = "p_url")
    private String pUrl;

    @Column(name = "p_name")
    private String pName;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pId=").append(pId);
        sb.append(", pUrl=").append(pUrl);
        sb.append(", pName=").append(pName);
        sb.append("]");
        return sb.toString();
    }
}