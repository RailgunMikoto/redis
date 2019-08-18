package com.wxy.redis.controller;

import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("product")
@Api(value = "商品接口", description = "商品相关操作")
public class ProductController {

    @Resource
    ProductService productService;

    @ResponseBody
    @RequestMapping("getInfo/{productId}")
    @ApiOperation(value = "查询商品信息", response = ResultResponse.class, httpMethod = "GET")
    public ResultResponse getInfo(@PathVariable("productId") @ApiParam(value = "商品id", required = true) Integer productId){
        log.info("ProductController: getInfo -> productId = {}", productId);
        return productService.getInfo(productId);
    }

    @ResponseBody
    @RequestMapping("getProductByCid/{cid}/{currentPage}/{pageSize}")
    @ApiOperation(value = "根据分类查找商品", response = ResultResponse.class, httpMethod = "GET")
    public ResultResponse getProductByCid(@PathVariable("cid") @ApiParam(value = "商品分类id") Integer cId,
                                          @PathVariable("currentPage") @ApiParam(value = "当前页")Integer currentPage,
                                          @PathVariable("pageSize") @ApiParam(value = "分页尺寸") Integer pageSize){
        return productService.getProductByCid(cId, currentPage, pageSize);
    }

}
