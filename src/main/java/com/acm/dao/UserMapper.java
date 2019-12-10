package com.acm.dao;

import com.acm.common.utils.MyMapper;
import com.acm.pojo.db.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends MyMapper<User> {

    User getUser(@Param("userName") String userName, @Param("password") String password);

    User getUserById(@Param("uId") long uId);

    int getUserByUserName(@Param("userName") String userName);

    void updateURank(@Param("uId") long uId, @Param("newRank") int newRank);

    void updatePassword(@Param("uId") long uId, @Param("password") String password);
}