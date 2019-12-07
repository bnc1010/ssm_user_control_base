package com.acm.service.impl;

import com.acm.dao.RoleMapper;
import com.acm.dao.RolePermissionMapper;
import com.acm.dao.UserRoleMapper;
import com.acm.pojo.db.Role;
import com.acm.pojo.db.RolePermission;
import com.acm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void grantPrivileges(int rId, List<Integer> pIds) {
        for (int pid : pIds){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setpId(pid);
            rolePermission.setrId(rId);
            try {
                rolePermissionMapper.insert(rolePermission);
            }
            catch (Exception e){

            }
        }
    }

    @Override
    public boolean checkRoleExist(int rId) {
        return roleMapper.existsWithPrimaryKey(rId);
    }

    @Override
    public List<Role> getCommonRole(){
        return  roleMapper.getCommonRole();
    }

    @Override
    public void robRole(int rId) {
        userRoleMapper.robRole(rId);
        rolePermissionMapper.deleteByRoleId(rId);
    }

    @Override
    public List<Integer> getUserIdByRoleId(int rId) {
        return userRoleMapper.getUserIdByRoleId(rId);
    }

    @Override
    public List<Role> getRoleByUserId(long uId) {
        List<Integer> rIds = userRoleMapper.getRoleIdOfByUserId(uId);
        List<Role> roles = new ArrayList<>();
        for (int rId : rIds){
            roles.add(roleMapper.selectByPrimaryKey(rId));
        }
        return roles;
    }

    @Override
    public List<Integer> getPermissionIdByRoleId(int rId) {
        return rolePermissionMapper.getPermissionIdOfByRoleId(rId);
    }

    @Override
    public void deleteByRoleIdAndPermissionId(int rId, int pId) {
        rolePermissionMapper.deleteByRoleIdAndPermissionId(rId, pId);
    }


}
