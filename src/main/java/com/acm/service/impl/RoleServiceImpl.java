package com.acm.service.impl;

import com.acm.dao.RoleMapper;
import com.acm.dao.RolePermissionMapper;
import com.acm.pojo.db.Role;
import com.acm.pojo.db.RolePermission;
import com.acm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Autowired
    private RolePermissionMapper rolePermissionMapper;

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
}
