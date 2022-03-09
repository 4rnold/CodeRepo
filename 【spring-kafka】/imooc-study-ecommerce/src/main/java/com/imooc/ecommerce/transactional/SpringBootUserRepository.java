package com.imooc.ecommerce.transactional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <h1>JpaSpringBootUser Dao 接口定义</h1>
 * */
public interface SpringBootUserRepository extends JpaRepository<JpaSpringBootUser, Integer> {
}
