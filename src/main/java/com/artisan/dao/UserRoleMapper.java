package com.artisan.dao;

import com.artisan.common.utils.MyMapper;
import com.artisan.pojo.db.Role;
import com.artisan.pojo.db.UserRole;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {

    List<Integer> getRoleIdOfByUserId(@Param("userId") long userId);

}