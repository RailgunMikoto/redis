package com.wxy.redis.service.impl;

import com.google.common.collect.Lists;
import com.wxy.redis.common.*;
import com.wxy.redis.dto.CategoryProductDto;
import com.wxy.redis.dto.ProductDto;
import com.wxy.redis.entity.Product;
import com.wxy.redis.entity.ProductWithBLOBs;
import com.wxy.redis.mapper.CategoryMapper;
import com.wxy.redis.mapper.ProductMapper;
import com.wxy.redis.service.ProductService;
import com.wxy.redis.util.CategoryIdUtil;
import com.wxy.redis.util.KeyUtil;
import com.wxy.redis.util.PageHelper;
import com.wxy.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取商品详细信息
     * @param productId 商品id
     * @return
     */
    @Override
    public ResultResponse<ProductWithBLOBs> getInfo(Integer productId) {
        if (productId == null){
            log.info("ProductServiceImpl 方法: getInfo -> {}", ResultEnum.PARAM_ERROR.getMessage());
            return ResultResponse.fail(ResultEnum.PARAM_ERROR.getMessage());
        }
        // 生成key
        String key = KeyUtil.getKey(RedisKey.PRODUCT_INFO, productId + "");
        // 从redis中获取值
        ProductWithBLOBs product = (ProductWithBLOBs)redisUtil.get(key);
        // 判断
        if (product == null){
            log.info("ProductServiceImpl 方法: getInfo -> 缓存中没有数据，从数据库查询");
            product = productMapper.selectByPrimaryKey(productId);
            if (product == null){
                return ResultResponse.fail(ProductInfoEnum.NOT_EXISTS.getMessage());
            }
            log.info("ProductServiceImpl 方法: getInfo -> 将数据存入redis缓存");
            redisUtil.setex(key, product, RedisTime.HALF_HOUR.getTime());
        }
        // 返回数据
        return ResultResponse.success(product);
    }

    /**
     * 根据分类id查询当前分类下所有商品，并分页显示
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public ResultResponse<CategoryProductDto> getProductByCid(Integer cid, Integer currentPage, Integer pageSize) {

        if (cid == null){
            return ResultResponse.fail();
        }
        if (currentPage.intValue() < 0){
            currentPage = 1;
        }
        if (pageSize.intValue() < 0){
            pageSize = 10;
        }
        // 获取redis的key值
        String key = KeyUtil.getKey(RedisKey.CATEGORY_PRODUCT_INFO, cid + "");
        // 从redis中获取键为key的数据
        Object obj = redisUtil.get(key);
        // 判断
        if (obj == null){
            log.info("ProductServiceImpl 方法: getProductByCid -> redis中没有数据，从数据库中读取");
            // 添加cid到存储cid的集合中
            CategoryIdUtil.setFirst(Lists.newArrayList(cid));
            // 查询该分类下所有包含的分类
            getChildId(Lists.newArrayList(cid));
            // 根据分类id查询商品
            List<Product> byCids = productMapper.findByCids(CategoryIdUtil.get());

            if (byCids == null || byCids.isEmpty()){
                return ResultResponse.fail(ProductInfoEnum.NOT_EXISTS.getMessage());
            }
            // 将Product转换为ProductDto
            List<ProductDto> productDtos = byCids.parallelStream().map(ProductDto::convert).collect(Collectors.toList());
            // 获取分页对象
            PageHelper page = PageHelper.getPage(productDtos, currentPage, pageSize);
            // 创建CategoryProductDto
            CategoryProductDto categoryProductDto = CategoryProductDto.builder()
                    .currentPage(page.getPage())
                    .data(page.getData())
                    .pageCount(page.getPageCount())
                    .pageSize(page.getSize())
                    .totalCount(page.getTotalCount()).build();
            // 释放存储cid的集合
            CategoryIdUtil.relase();
            CategoryIdUtil.binding();
            log.info("ProductServiceImpl 方法: getProductByCid -> 将数据存入redis");
            redisUtil.setex(key, categoryProductDto, RedisTime.HALF_HOUR.getTime());
            // 返回数据
            return ResultResponse.success(categoryProductDto);
        }else {
            log.info("ProductServiceImpl 方法: getProductByCid -> 从redis中读取到数据");
            CategoryProductDto categoryProductDto = (CategoryProductDto) obj;
            log.info("ProductServiceImpl 方法: getInfo -> categoryProductDto = {}", categoryProductDto);
            // 返回数据
            return ResultResponse.success(categoryProductDto);
        }
    }

    /**
     * 获取传入的分类的所有下级分类的id
     * @param cids
     * @return
     */
    private void getChildId(List<Integer> cids){
        if (cids != null && !cids.isEmpty()){
            for (Integer cid : cids) {
                // 查询当前cid的孩子id
                List<Integer> childIds = categoryMapper.getChildIds(cid);
                // 添加孩子id到存储cid的集合中
                CategoryIdUtil.setSeconds(childIds);
                // 递归查询子类别id
                getChildId(childIds);
            }
        }
    }
}
