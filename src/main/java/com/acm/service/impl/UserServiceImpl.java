package com.acm.service.impl;

import com.acm.dao.RoleMapper;
import com.acm.dao.UserMapper;
import com.acm.dao.UserRoleMapper;
import com.acm.pojo.db.Role;
import com.acm.pojo.db.User;
import com.acm.pojo.db.UserRole;
import com.acm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * IUserService接口实现类
 * @author Leeyom Wang
 * @date 2017年10月26日 15:15
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserRoleMapper userRoleMapper;
    /**
     * 根据用户名和密码获取用户信息
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User getUser(String userName, String password) {
        return userMapper.getUser(userName,password);
    }

    @Override
    public boolean isExist(String userName) {
//        System.out.println(userName);
        return userMapper.getUserByUserName(userName) > 0;
    }

    @Override
    public void giveCommonRole(long uId) {
        List<Role> commonRoles = roleMapper.getCommonRole();
        for (Role role : commonRoles){
            userRoleMapper.insertUserRole(uId, role.getrId());
        }
    }

    public void grantPrivileges(int uId, List<Integer> rIds) {
        for (int rId : rIds){
            UserRole userRole = new UserRole();
            userRole.setrId(rId);
            userRole.setuId(uId);
            try {
                userRoleMapper.insert(userRole);
            }
            catch (Exception e){

            }
        }
    }
}
