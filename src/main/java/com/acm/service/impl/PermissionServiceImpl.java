package com.acm.service.impl;

import com.acm.dao.PermissionMapper;
import com.acm.dao.RolePermissionMapper;
import com.acm.pojo.db.Permission;
import com.acm.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("permissionService")

public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements IPermissionService {
    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Override
    public int getIdByNameAndUrl(String pName, String pUrl) {
        return permissionMapper.getIdByNameAndUrl(pName,pUrl);
    }

    @Override
    public void robPermission(int pId) {
        rolePermissionMapper.robPermission(pId);
    }
}
