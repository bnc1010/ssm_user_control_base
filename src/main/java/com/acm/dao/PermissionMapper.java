package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.Permission;
import org.apache.ibatis.annotations.Param;

public interface PermissionMapper extends MyMapper<Permission> {

    Permission getPermissionById(@Param("pid") int pid);

}