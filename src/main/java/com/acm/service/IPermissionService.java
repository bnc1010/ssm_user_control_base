package com.acm.service;

import com.acm.pojo.db.Permission;

public interface IPermissionService extends IBaseService<Permission> {

    int getIdByNameAndUrl(String pName,String pUrl);

    void robPermission(int pId);
}
