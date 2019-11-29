package com.artisan.dao;

import com.artisan.common.utils.MyMapper;
import com.artisan.pojo.db.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends MyMapper<Permission> {

    Permission getPermissionById(@Param("pid") int pid);

}