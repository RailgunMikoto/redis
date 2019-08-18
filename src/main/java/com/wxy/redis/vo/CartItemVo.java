package com.wxy.redis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemVo implements Serializable {

    private Integer productId;//商品id
    private Integer quantity;//购物车中此商品的数量
    private String productName;//商品名称
    private String productSubtitle;//主标题
    private String productMainImage;//主图片
    private BigDecimal productPrice;//单价
    private Integer productStatus;//商品状态，因为该商品可能在加入购物车后才下架
    private BigDecimal productTotalPrice;//该项的总价格
    private Integer productChecked;//此商品是否勾选，为订单生成做准备

}
