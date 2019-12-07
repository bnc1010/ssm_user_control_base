package com.acm.pojo.vo;

public class RoleVO {
    /**
     * 主键
     */
    private Integer rId;

    /**
     * 角色名
     */
    private String roleName;


    private String rType;

    private Integer pageNum;

    private Integer pageSize;

    /**
     * 角色对应权限码的数组
     */
    private String [] pCodes;


    private String token;

    private Integer rRank;

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

    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
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

    public String[] getpCodes() {
        return pCodes;
    }

    public void setpCodes(String[] pCodes) {
        this.pCodes = pCodes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setrRank(Integer rRank) {
        this.rRank = rRank;
    }

    public Integer getrRank() {
        return rRank;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "rId=" + rId +
                ", roleName='" + roleName + '\'' +
                ", isAdmin=" + rType +
                '}';
    }
}
