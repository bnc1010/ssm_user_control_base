package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.RolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RolePermissionMapper extends MyMapper<RolePermission> {

    List<Integer> getPermissionIdOfByRoleId(@Param("roleId") int roleId);

    void robPermission(@Param("pId") int pId);

    void deleteByRoleId(@Param("rId") int rId);

    void deleteByRoleIdAndPermissionId(@Param("rId") int rId, @Param("pId") int pId);
}