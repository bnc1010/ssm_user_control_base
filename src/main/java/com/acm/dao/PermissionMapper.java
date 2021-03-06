package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface PermissionMapper extends MyMapper<Permission> {

    Permission getPermissionById(@Param("pid") int pid);
    int getIdByNameAndUrl(@Param("pName") String pName,@Param("pUrl") String pUrl);
    void insertPermission(@Param("pName") String pName,@Param("pUrl") String pUrl,@Param("pType") String pType);
}