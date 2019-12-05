package com.acm.service.impl;

import com.acm.dao.PermissionMapper;
import com.acm.pojo.db.Permission;
import com.acm.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("permissionService")

public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements IPermissionService {
    @Autowired
    PermissionMapper permissionMapper;
}
