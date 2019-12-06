package com.acm.service;

import com.acm.pojo.db.Role;

import java.util.List;

public interface IRoleService extends IBaseService<Role>{
    void grantPrivileges(int rId, List<Integer> pIds);

    boolean checkRoleExist(int rId);
}
