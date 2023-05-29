package com.itheima.pay.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PayDao {

	@Update("update pay set ispay=#{ispay} where id=#{id};")
	int update(@Param("id") String id, @Param("ispay") Integer ispay);

}
