package com.wxy.redis.mapper;

import com.wxy.redis.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    // 根据userId查询购物车
    List<Cart> findByUserId(@Param("userId") Integer userId);

    // 插入一条数据
    void save(Cart cart);

    // 修改一条数据
    void update(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("quantity") Integer quantity);

}
