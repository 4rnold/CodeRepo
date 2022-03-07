package com.arnold.ecommerce.controller;


import com.arnold.ecommerce.common.TableId;
import com.arnold.ecommerce.goods.DeductGoodsInventory;
import com.arnold.ecommerce.goods.GoodsInfo;
import com.arnold.ecommerce.goods.SimpleGoodsInfo;
import com.arnold.ecommerce.service.IGoodsService;
import com.arnold.ecommerce.vo.PageSimpleGoodsInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>商品微服务对外暴露的功能服务 API 接口</h1>
 * */
@Api(tags = "商品微服务功能接口")
@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final IGoodsService goodsService;

    public GoodsController(IGoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @ApiOperation(value = "详细商品信息", notes = "根据 TableId 查询详细商品信息",
            httpMethod = "POST")
    @PostMapping("/goods-info")
    public List<GoodsInfo> getGoodsInfoByTableId(@RequestBody TableId tableId) {
        return goodsService.getGoodsInfoByTableId(tableId);
    }

    @ApiOperation(value = "简单商品信息", notes = "获取分页的简单商品信息", httpMethod = "GET")
    @GetMapping("/page-simple-goods-info")
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(
            @RequestParam(required = false, defaultValue = "1") int page) {
        return goodsService.getSimpleGoodsInfoByPage(page);
    }

    @ApiOperation(value = "简单商品信息", notes = "根据 TableId 查询简单商品信息",
            httpMethod = "POST")
    @PostMapping("/simple-goods-info")
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId) {
        return goodsService.getSimpleGoodsInfoByTableId(tableId);
    }

    @ApiOperation(value = "扣减商品库存", notes = "扣减商品库存", httpMethod = "PUT")
    @PutMapping("/deduct-goods-inventory")
    public Boolean deductGoodsInventory(
            @RequestBody List<DeductGoodsInventory> deductGoodsInventories) {
        return goodsService.deductGoodsInventory(deductGoodsInventories);
    }


}
