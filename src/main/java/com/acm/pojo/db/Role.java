package com.acm.pojo.db;

import javax.persistence.*;

public class Role {
    /**
     * 主键
     */
    @Id
    @Column(name = "r_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rId;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;


    @Column(name = "r_type")
    private String rType;

    @Column(name = "r_rank")
    private int rRank;
    /**
     * 获取主键
     *
     * @return r_id - 主键
     */
    public Integer getrId() {
        return rId;
    }

    /**
     * 设置主键
     *
     * @param rId 主键
     */
    public void setrId(Integer rId) {
        this.rId = rId;
    }

    /**
     * 获取角色名
     *
     * @return role_name - 角色名
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名
     *
     * @param roleName 角色名
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public String getRType() {
        return rType;
    }

    public void setRType(String rType) {
        this.rType = rType;
    }

    public int getrRank() {
        return rRank;
    }

    public void setrRank(int rRank) {
        this.rRank = rRank;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rId=").append(rId);
        sb.append(", roleName=").append(roleName);
        sb.append("]");
        return sb.toString();
    }
}