package com.wxy.redis.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wxy.redis.common.CartInfoEnum;
import com.wxy.redis.common.ProductInfoEnum;
import com.wxy.redis.common.ResultEnum;
import com.wxy.redis.common.ResultResponse;
import com.wxy.redis.entity.Cart;
import com.wxy.redis.entity.ProductWithBLOBs;
import com.wxy.redis.mapper.CartMapper;
import com.wxy.redis.mapper.ProductMapper;
import com.wxy.redis.service.CartService;
import com.wxy.redis.service.ProductService;
import com.wxy.redis.util.BigDecimalUtil;
import com.wxy.redis.util.RedisUtil;
import com.wxy.redis.vo.CartItemVo;
import com.wxy.redis.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProductService productService;

    /**
     * 添加商品到购物车
     * 1、查询此商品是否存在
     * 2、查询购物车是否存在或者里面是否存在此商品
     *      1)、购物车不存在，判断商品数量与库存
     *      2)、购物车存在，判断里面是否存在此商品，如果此商品存在，判断商品总数量与库存
     * 3、将购物车添加到数据库中
     * 4、删除redis中的购物车缓存
     * @param productId
     * @param quantity
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public ResultResponse addProduct(Integer productId, Integer quantity, Integer userId) {

        // 从数据库查询你商品信息
        ProductWithBLOBs product = productMapper.selectByPrimaryKey(productId);
        // 判断商品是否存在
        if (product == null){
            return ResultResponse.fail(ProductInfoEnum.NOT_EXISTS.getMessage());
        }
        // 从数据库中查询购物车
        List<Cart> carts = cartMapper.findByUserId(userId);
        if (carts == null || carts.isEmpty()){
            // 如果购物车为空或者不存在购物车
            if (product.getStock() < quantity){
                // 商品库存小于添加数量
                return ResultResponse.fail(ProductInfoEnum.STOCK_SHORTAGE.getMessage());
            }
            // 构建Cart对象，存入数据库
            Cart cart = Cart.builder()
                    .checked(CartInfoEnum.IS_CHECKED.getCode())
                    .productId(productId)
                    .quantity(quantity)
                    .userId(userId).build();
            cartMapper.save(cart);
        } else {
            // 判断购物车中是否存在此商品
            List<Cart> exists = carts.stream().filter(cart -> cart.getProductId() == productId).collect(Collectors.toList());
            if (exists == null || exists.isEmpty()){
                // 如果购物车为空或者不存在购物车
                if (product.getStock() < quantity){
                    // 商品库存小于添加数量
                    return ResultResponse.fail(ProductInfoEnum.STOCK_SHORTAGE.getMessage());
                }
                // 构建Cart对象，存入数据库
                Cart cart = Cart.builder()
                        .checked(CartInfoEnum.IS_CHECKED.getCode())
                        .productId(productId)
                        .quantity(quantity)
                        .userId(userId).build();
                cartMapper.save(cart);
            }else {
                // 购物车存在此商品
                Cart cart = exists.get(0);
                if (product.getStock() < (cart.getQuantity() + quantity)){
                    // 商品库存小于添加数量
                    return ResultResponse.fail(ProductInfoEnum.STOCK_SHORTAGE.getMessage());
                }
                // 修改购物车中相关商品数量
                // 修改数据到数据库中
                cartMapper.update(userId, productId, cart.getQuantity() + quantity);
            }
        }
        // TODO：修改购物车后，删除redis中购物车的缓存

        return ResultResponse.success();
    }

    /**
     * 查询用户购物车信息
     * 1、从redis缓存中查询
     * 2、缓存中没有数据则从数据库查询
     * 3、将查询出来的数据存入缓存
     * 4、根据数据查询商品信息
     * @param userId
     * @return
     */
    @Override
    public ResultResponse queryCartInfo(Integer userId) {

        // 从缓存中获取当前用户的购物车信息
        Map<Object, Object> productIdAndQuantity = redisUtil.hmget(userId+CartInfoEnum.QUANTITY.getMessage());
        Map<Object, Object> productIdAndChecked = redisUtil.hmget(userId+CartInfoEnum.CHECKED.getMessage());
        if (CollectionUtils.isEmpty(productIdAndQuantity) || CollectionUtils.isEmpty(productIdAndChecked)){
            // redis中的数据为空，从数据库查询
            List<Cart> carts = cartMapper.findByUserId(userId);
            if (CollectionUtils.isEmpty(carts)){
                // 没有数据，返回信息：购物车空空如也
                return ResultResponse.fail(CartInfoEnum.IS_EMPTY.getMessage());
            }
            productIdAndQuantity = Maps.newHashMap();
            productIdAndChecked = Maps.newHashMap();
            for (Cart cart:carts) {
                productIdAndQuantity.put(String.valueOf(cart.getProductId()), cart.getQuantity());
                productIdAndChecked.put(String.valueOf(cart.getProductId()), cart.getChecked());
            }
            // 存入redis缓存中
            redisUtil.hmset(userId+CartInfoEnum.QUANTITY.getMessage(), productIdAndQuantity);
            redisUtil.hmset(userId+CartInfoEnum.CHECKED.getMessage(), productIdAndChecked);

        }
        // 存储所有商品
        List<ProductWithBLOBs> products = Lists.newArrayList();
        Set<Map.Entry<Object, Object>> entries = productIdAndQuantity.entrySet();
        for (Map.Entry<Object, Object> entry:entries) {
            ResultResponse<ProductWithBLOBs> resultResponse = productService.getInfo(Integer.valueOf(String.valueOf(entry.getKey())));
            if (resultResponse.getCode().equals(ResultEnum.SUCCESS.getCode())){
                products.add(resultResponse.getData());
            }
        }
        if (CollectionUtils.isEmpty(products)){
            // 没有数据，返回信息：购物车空空如也
            return ResultResponse.fail(CartInfoEnum.IS_EMPTY.getMessage());
        }
        // 购物车的总价格
        BigDecimal totalPrice = new BigDecimal("0");
        // 购物车是否全部选中
        Map<String, Integer> checked = Maps.newHashMap();
        checked.put("checked", CartInfoEnum.IS_CHECKED.getCode());
        // 临时变量
        Map<Object, Object> tempQuantity = productIdAndQuantity;
        Map<Object, Object> tempChecked = productIdAndChecked;
        // 返回的CartItemVos
        List<CartItemVo> cartItemVos = Lists.newArrayList();
        for (ProductWithBLOBs product:products) {
            CartItemVo cartItemVo = new CartItemVo();
            cartItemVo.setProductId(product.getId());
            cartItemVo.setProductMainImage(product.getMainImage());
            cartItemVo.setProductName(product.getName());
            cartItemVo.setProductPrice(product.getPrice());
            cartItemVo.setProductStatus(product.getStatus());
            cartItemVo.setProductSubtitle(product.getSubtitle());
            cartItemVo.setProductChecked((Integer) tempChecked.get(String.valueOf(product.getId())));
            cartItemVo.setQuantity((Integer) tempQuantity.get(String.valueOf(product.getId())));
            cartItemVo.setProductTotalPrice(BigDecimalUtil.multi(cartItemVo.getProductPrice(), cartItemVo.getQuantity()));
            if (cartItemVo.getProductChecked().equals(CartInfoEnum.IS_CHECKED.getCode())){
                // 如果选中了此商品，加商总价格
                totalPrice = BigDecimalUtil.add(totalPrice, cartItemVo.getProductTotalPrice());
            } else {
                checked.put("checked", CartInfoEnum.NOT_CHECKD.getCode());
            }
            cartItemVos.add(cartItemVo);
        }
        // 返回的CartVo
        CartVo cartVo = new CartVo();
        cartVo.setCartItemVoList(cartItemVos);
        cartVo.setUserId(userId);
        cartVo.setAllChecked(checked.get("checked").equals(CartInfoEnum.IS_CHECKED.getCode()) ? true : false);
        cartVo.setCartTotalPrice(totalPrice);

        return ResultResponse.success(cartVo);
    }
}
