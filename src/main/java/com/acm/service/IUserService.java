package com.acm.service;

import com.acm.pojo.db.Role;
import com.acm.pojo.db.User;

import java.util.List;

/**
 * User的service接口层
 * @author Leeyom Wang
 * @date 2017年10月26日 15:14
 */
public interface IUserService extends IBaseService<User> {

    /**
     * 根据用户名和密码获取用户信息
     * @param userName
     * @param password
     * @return
     */
    User getUser(String userName, String password);

    User getUser(long uId);

    boolean isExist(String userName);
    /**
     * @author bnc
     *
     */

    void giveCommonRole(long uId, List<Role> commonRole);

    void grantPrivileges(int uId, List<Integer> rIds);

    void updateURank(int uId, int newRank);

    void deleteByUserIdAndRoleId(int uId, int rId);

    void deleteByUserId(int uId);

    void resetPassword(int uId, String password);
}
