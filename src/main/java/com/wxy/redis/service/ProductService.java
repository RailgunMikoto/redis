package com.wxy.redis.service;

import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.dto.CategoryProductDto;
import com.wxy.redis.entity.ProductWithBLOBs;

public interface ProductService {

    // 获取商品信息
    ResultResponse<ProductWithBLOBs> getInfo(Integer productId);

    // 根据分类id获取商品信息
    ResultResponse<CategoryProductDto> getProductByCid(Integer cid, Integer currentPage, Integer pageSize);


}
