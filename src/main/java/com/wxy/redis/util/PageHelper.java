package com.wxy.redis.util;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页助手
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageHelper<T> {

    // 数据总条数
    private Integer totalCount = 0;
    // 总页数
    private Integer pageCount  = 0;
    // 当前页
    private Integer page = 0;
    // 当前页面显示条数
    private Integer currentSize = 0;
    // 设置页面显示条数
    private Integer size = 0;
    // 偏移量
    private Integer offset = 0;
    // 是否存在下一页
    private boolean nextPage = false;
    // 是否是最后一页
    private boolean lastPage = false;
    // 是否为第一页
    private boolean firstPage = false;
    // 当前页是否超过总页数
    private boolean outOfPage = false;
    // 分页显示的数据
    private List<T> data;

    public static <T>PageHelper getPage(List<T> list, Integer currentPage, Integer pageSize){
        PageHelper<T> pageHelper = new PageHelper<>();
        if (list == null || list.isEmpty()){
            return pageHelper;
        }
        // 设置总条数
        pageHelper.setTotalCount(list.size());
        // 设置当前页
        pageHelper.setPage(currentPage);
        // 设置当前页面显示条数
        pageHelper.setCurrentSize(pageSize);
        // 设置页面显示条数
        pageHelper.setSize(pageSize);
        // 设置偏移量
        pageHelper.setOffset((currentPage - 1) * pageSize);
        // 设置总页数
        pageHelper.setPageCount((int)Math.ceil((1.0 * pageHelper.getTotalCount() / pageHelper.getSize())));
        // 设置是否为第一页
        if (pageHelper.getPage() == 1){
            pageHelper.setFirstPage(true);
        }
        // 设置是否为最后一页
        if (pageHelper.getPage().equals(pageHelper.getPageCount())){
            pageHelper.setLastPage(true);
        }else {
            // 当前页是否超过最大页数
            if (pageHelper.getPage().intValue() > pageHelper.getPageCount().intValue()){
                pageHelper.setOutOfPage(true);
            }else{
                // 存在下一页
                pageHelper.setNextPage(true);
            }
        }
        // 如果只有一页
        if (pageHelper.lastPage && pageHelper.firstPage){
            // 设置分页后的数据
            pageHelper.setData(list);
            // 设置当前页数据条数
            pageHelper.setCurrentSize(pageHelper.getData().size());
        }else {
            // 如果是最后一页
            if (pageHelper.lastPage){
                // 设置分页后的数据
                pageHelper.setData(list.subList(pageHelper.getOffset(), pageHelper.getTotalCount()));
                // 设置当前页数据条数
                pageHelper.setCurrentSize(pageHelper.getData().size());
            }else {
                // 查看当前页码是否超过最大页数
                if (pageHelper.outOfPage){
                    // 设置数据集为空集合
                    pageHelper.setData(Lists.newArrayList());
                    // 设置当前页数据条数
                    pageHelper.setCurrentSize(pageHelper.getData().size());
                } else {
                    // 中间页
                    // 设置分页后的数据
                    pageHelper.setData(list.subList(pageHelper.getOffset(), pageHelper.getSize() + pageHelper.getOffset()));
                }
            }
        }

        return pageHelper;
    }

}
