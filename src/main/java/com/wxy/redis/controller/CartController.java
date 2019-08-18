package com.wxy.redis.controller;

import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("cart")
@Api(value = "购物车接口", description = "购物车相关操作")
public class CartController {

    @Autowired
    private CartService cartService;

    @ResponseBody
    @PostMapping("addProduct/{productId}/{quantity}")
    @ApiOperation(value = "添加商品到购物车", httpMethod = "POST", response = ResultResponse.class)
    public ResultResponse addProduct(@ApiParam(value = "商品id", required = true) @PathVariable("productId") Integer productId,
                                     @ApiParam(value = "商品数量", required = true) @PathVariable("quantity") Integer quantity){

        return cartService.addProduct(productId, quantity, 21);
    }

    @ResponseBody
    @GetMapping("queryCartInfo")
    @ApiOperation(value = "查询用户购物车信息", httpMethod = "GET", response = ResultResponse.class)
    public ResultResponse queryCartInfo(){
        return cartService.queryCartInfo(21);
    }
}
