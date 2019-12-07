package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRoleMapper extends MyMapper<UserRole> {

    List<Integer> getRoleIdOfByUserId(@Param("userId") long userId);

    void insertUserRole(@Param("userId") long userId, @Param("roleId") int roleId);

    void robRole(@Param("rId") int rId);

    List<Integer> getUserIdByRoleId(@Param("rId") int rId);

    void deleteByUserIdAndRoleId(@Param("uId") int uId, @Param("rId") int rId);


}