package com.acm.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用的Service
 * @author leeyom
 */
@Service
public interface IBaseService<T> {

    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    T selectByPrimaryKey(Integer id);

    List<T> selectAll(int pageNum, int pageSize);

    int updateByPrimaryKey(T record);

}
