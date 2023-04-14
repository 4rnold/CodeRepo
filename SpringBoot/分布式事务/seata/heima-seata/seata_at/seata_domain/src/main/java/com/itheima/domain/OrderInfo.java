package com.itheima.domain;

import java.io.Serializable;
import java.util.Date;

/***
 *
 *
 ****/
public class OrderInfo implements Serializable {
    private String id;
    private Long money;
    private Date createtime;
    private String usernumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }
}