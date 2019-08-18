package com.wxy.redis.mapper;

import com.wxy.redis.entity.Product;
import com.wxy.redis.entity.ProductWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductWithBLOBs record);

    int insertSelective(ProductWithBLOBs record);

    ProductWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProductWithBLOBs record);

    int updateByPrimaryKey(Product record);

    List<ProductWithBLOBs> findAll();

    List<Product> findByCids (@Param("cids") List<Integer> cids);

    List<ProductWithBLOBs> findByIds(@Param("ids") List<Integer> productIds);
}