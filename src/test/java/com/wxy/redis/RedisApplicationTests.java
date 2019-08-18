package com.wxy.redis;

import com.google.common.collect.Maps;
import com.wxy.redis.common.CartInfoEnum;
import com.wxy.redis.entity.ProductWithBLOBs;
import com.wxy.redis.mapper.CategoryMapper;
import com.wxy.redis.mapper.ProductMapper;
import com.wxy.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisApplicationTests {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void contextLoads() {

        Map<Object, Object> map = Maps.newHashMap();
        map.put(String.valueOf(1), 10);

        String key = "test";
        redisUtil.hmset(key, map);
        Map<Object, Object> test = redisUtil.hmget(key);
        Object o1 = test.get(1);
        Object o2 = test.get(String.valueOf(1));
        Integer o3 = (Integer) test.get(1);
        Integer o4 = (Integer) test.get(String.valueOf(1));
        System.out.println("yes");
    }

}
