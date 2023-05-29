package com.itheima.pay.domain;

import java.io.Serializable;

public class Pay implements Serializable{

	private String id;

	private Integer ispay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIspay() {
		return ispay;
	}

	public void setIspay(Integer ispay) {
		this.ispay = ispay;
	}
}
