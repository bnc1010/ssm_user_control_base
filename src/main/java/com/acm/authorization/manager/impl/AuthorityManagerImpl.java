package com.acm.authorization.manager.impl;

import com.acm.authorization.manager.AuthorityManager;
import com.acm.dao.PermissionMapper;
import com.acm.dao.RolePermissionMapper;
import com.acm.dao.UserRoleMapper;
import com.acm.pojo.db.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorityManagerImpl implements AuthorityManager {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Autowired
    PermissionMapper permissionMapper;

    /**
     * @param userId, @param target:target api url
     * 检查权限
     **/
    @Override
    public boolean checkAuthority(long userId, String target) {
        List<Integer> roles = userRoleMapper.getRoleIdOfByUserId(userId);
        for (int roleId : roles){
            List<Integer> permissionIds = rolePermissionMapper.getPermissionIdOfByRoleId(roleId);
            for (int permissionId : permissionIds){
                Permission permission = permissionMapper.getPermissionById(permissionId);
                if (target.contains(permission.getpUrl())){
                    return true;
                }
            }
        }
        return false;
    }
}
