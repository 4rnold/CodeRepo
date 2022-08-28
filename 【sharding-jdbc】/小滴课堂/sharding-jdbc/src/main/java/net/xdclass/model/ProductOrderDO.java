package net.xdclass.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@Data
@TableName("product_order")
@EqualsAndHashCode(callSuper = false)
public class ProductOrderDO {

    //@TableId(value = "id",type = IdType.AUTO)
    //@TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    private String outTradeNo;

    private String state;

    private Date createTime;

    private Double payAmount;

    private String nickname;

    private Long userId;

}
