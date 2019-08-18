package com.wxy.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {

    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    // 1：选中  0：不选
    private Integer checked;

}
