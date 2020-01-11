package com.acm.pojo.vo;


import java.util.Date;

/**
 * User视图层实体类对象
 * @author leeyom
 * @date 2017年10月30日 21:15
 */
public class UserVO {

    /**
     * 主键
     */
    private Integer uId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;


    private Integer uRank;

    private Integer [] rIds;
    /**
     * token
     */

    private Integer pageNum;

    private Integer pageSize;

    private String token;

    private String password;

    private String [] rCodes;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String[] getrCodes() {
        return rCodes;
    }

    public void setrCodes(String[] rCodes) {
        this.rCodes = rCodes;
    }

    public void setuRank(Integer uRank) {
        this.uRank = uRank;
    }

    public Integer getuRank() {
        return uRank;
    }

    public Integer [] getrIds() {
        return rIds;
    }

    public void setrIds(Integer [] rIds) {
        this.rIds = rIds;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "uId=" + uId +
                ", uName='" + userName + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", age=" + age +
                ", token='" + token + '\'' +
                '}';
    }
}
