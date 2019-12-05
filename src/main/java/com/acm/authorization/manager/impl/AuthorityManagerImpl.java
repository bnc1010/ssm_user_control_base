package com.acm.authorization.manager.impl;

import com.acm.authorization.manager.AuthorityManager;
import com.acm.dao.PermissionMapper;
import com.acm.dao.RoleMapper;
import com.acm.dao.RolePermissionMapper;
import com.acm.dao.UserRoleMapper;
import com.acm.pojo.db.Permission;

import com.acm.pojo.db.Role;
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

    @Autowired
    RoleMapper roleMapper;

    /**
     * @param permissionCode 权限码
     * @param target:target api url
     * 检查权限
     **/
    @Override
    public boolean checkAuthority(String permissionCode, String target) {
//        System.out.println(authorityCode);
        String [] permissionCodes = permissionCode.split("&");
        for (String pc : permissionCodes){
            if (pc.equals("au:")){
                continue;
            }
            Permission permission = permissionMapper.getPermissionById(Integer.parseInt(pc.substring(1)));
            if (permission == null)return false;
            if (permission.getpUrl() != null){
                if(target.matches(permission.getpUrl())){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     *
     * @param userId
     * @return 权限码和角色码
     */
    @Override
    public String [] getAuthorityCode(long userId) {
        String [] ret = new String[2];
        Map<String, Boolean> mp = new HashMap<>();
        List<Integer> roles = userRoleMapper.getRoleIdOfByUserId(userId);
        for (int roleId : roles){
            List<Integer> permissionIds = rolePermissionMapper.getPermissionIdOfByRoleId(roleId);
            for (int permissionId : permissionIds){
                Permission permission = permissionMapper.getPermissionById(permissionId);
                if (!mp.containsKey(permission.getpType())){
                    mp.put(permission.getpType() + permission.getpId(), true);
                }
            }
        }
        String authorityCode = "au:";
        for (String key : mp.keySet()){
            authorityCode += "&";
            authorityCode += key;
        }
        ret[0] = authorityCode;

        authorityCode = "ru:";
        for (int roleId : roles){
            Role _role = roleMapper.selectByPrimaryKey(roleId);
            authorityCode += "&";
            authorityCode += _role.getRType() + _role.getrId();
        }
        ret[1] = authorityCode;
        return ret;
    }
}
