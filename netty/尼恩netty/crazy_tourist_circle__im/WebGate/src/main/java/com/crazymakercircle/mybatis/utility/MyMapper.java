package com.crazymakercircle.mybatis.utility;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author crazymakercircle
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>
{

}
