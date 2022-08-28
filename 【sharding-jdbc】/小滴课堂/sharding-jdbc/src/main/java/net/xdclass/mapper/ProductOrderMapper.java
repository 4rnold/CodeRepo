package net.xdclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xdclass.model.ProductOrderDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

public interface ProductOrderMapper extends BaseMapper<ProductOrderDO> {

    @Select("select * from product_order o left join product_order_item i on o.id=i.product_order_id")
    List<Object> listProductOrderDetail();
}
