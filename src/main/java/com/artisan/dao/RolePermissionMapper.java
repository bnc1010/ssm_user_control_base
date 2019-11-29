package com.artisan.dao;

import com.artisan.common.utils.MyMapper;
import com.artisan.pojo.db.RolePermission;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper extends MyMapper<RolePermission> {

    List<Integer> getPermissionIdOfByRoleId(@Param("roleId") int roleId);
}