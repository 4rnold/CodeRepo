package com.itheima.pay.service;

import com.itheima.pay.domain.Pay;

/**
 * 业务层接口
 */
public interface PayService {

	// 更新支付表中状态
	Integer update(Pay payTable);
}
