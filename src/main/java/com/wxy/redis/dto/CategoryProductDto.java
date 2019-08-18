package com.wxy.redis.dto;

import com.wxy.redis.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductDto implements Serializable {

    private Integer totalCount;

    private Integer pageCount;

    private Integer currentPage;

    private Integer pageSize;

    private List<ProductDto> data;

}
