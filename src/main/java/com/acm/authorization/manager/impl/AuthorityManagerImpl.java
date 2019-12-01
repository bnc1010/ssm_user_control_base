package com.acm.authorization.manager.impl;

import com.acm.authorization.manager.AuthorityManager;
import com.acm.dao.PermissionMapper;
import com.acm.dao.RolePermissionMapper;
import com.acm.dao.UserRoleMapper;
import com.acm.pojo.db.Permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthorityManagerImpl implements AuthorityManager {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Autowired
    PermissionMapper permissionMapper;

    /**
     * @param authorityCode 权限码
     * @param target:target api url
     * 检查权限
     **/
    @Override
    public boolean checkAuthority(String authorityCode, String target) {
        String [] authorityCodes = authorityCode.split("&");
        for (String ac : authorityCodes){
            if (ac.equals("au:")){
                continue;
            }
            Permission permission = permissionMapper.getPermissionByCode(ac);
            if (permission.getpUrl() != null){
                if(target.matches(permission.getpUrl())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getAuthorityCode(long userId) {
        Map<String, Boolean> mp = new HashMap<>();
        List<Integer> roles = userRoleMapper.getRoleIdOfByUserId(userId);
        for (int roleId : roles){
            List<Integer> permissionIds = rolePermissionMapper.getPermissionIdOfByRoleId(roleId);
            for (int permissionId : permissionIds){
                Permission permission = permissionMapper.getPermissionById(permissionId);
                if (!mp.containsKey(permission.getpCode())){
                    mp.put(permission.getpCode(), true);
                }
            }
        }
        String authorityCode = "au:";
        for (String key : mp.keySet()){
            authorityCode += "&";
            authorityCode += key;
        }
        return authorityCode;
    }
}
