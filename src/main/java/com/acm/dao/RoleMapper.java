package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.Role;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {
    List<Role> getCommonRole();
}