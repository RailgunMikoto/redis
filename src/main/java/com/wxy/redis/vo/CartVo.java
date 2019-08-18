package com.wxy.redis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartVo implements Serializable {

    private List<CartItemVo> cartItemVoList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private Integer userId;

}
