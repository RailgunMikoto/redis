package com.wxy.redis.util;


import java.util.List;

public class CategoryIdUtil {

    public static final ThreadLocal<List<Integer>> cids = new ThreadLocal<>();

    // 获取值
    public static List<Integer> get(){
        return cids.get();
    }

    // 第一次设置值
    public static void setFirst(List<Integer> idList){
        cids.set(idList);
    }

    public static void setSeconds(List<Integer> idList){
        cids.get().addAll(idList);
    }

    // 删除数据
    public static void relase(){
        cids.get().removeAll(cids.get());
    }

    // 解除绑定
    public static void binding(){
        cids.remove();
    }

}
