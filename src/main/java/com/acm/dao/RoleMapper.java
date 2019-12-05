package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface RoleMapper extends MyMapper<Role> {
    List<Role> getCommonRole();
}

