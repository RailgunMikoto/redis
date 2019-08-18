package com.wxy.redis.service;

import com.wxy.redis.common.ResultResponse;

public interface CartService {

    // 添加商品到购物车
    ResultResponse addProduct(Integer productId, Integer quantity, Integer userId);

    ResultResponse queryCartInfo(Integer userId);
}
