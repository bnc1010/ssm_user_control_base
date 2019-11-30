package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {

    List<Integer> getRoleIdOfByUserId(@Param("userId") long userId);

}