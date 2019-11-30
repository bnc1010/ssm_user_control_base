package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper extends MyMapper<RolePermission> {

    List<Integer> getPermissionIdOfByRoleId(@Param("roleId") int roleId);
}