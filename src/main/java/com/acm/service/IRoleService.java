package com.acm.service;

import com.acm.pojo.db.Role;
import io.swagger.models.auth.In;

import java.util.List;

public interface IRoleService extends IBaseService<Role>{
    void grantPrivileges(int rId, List<Integer> pIds);

    boolean checkRoleExist(int rId);

    List<Role> getCommonRole();

    void robRole(int rId);

    List<Integer> getUserIdByRoleId(int rId);

    List<Role> getRoleByUserId(long uId);

    List<Integer> getRoleIdByUserId(long uId);

    List<Integer> getPermissionIdByRoleId(int rId);

    void deleteByRoleIdAndPermissionId(int rId,int pId);
}
