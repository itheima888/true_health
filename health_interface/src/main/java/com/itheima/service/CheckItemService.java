package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项服务接口
 */
public interface CheckItemService {
    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项
     * @return
     */
    void add(CheckItem checkItem);
    /**
     * 分页查询
     * @return
     */
//    PageRequest findPage(Integer currentPage, Integer pageSize, String queryString);
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 删除检查项
     * @return
     */
    void delete(Integer checkItemId);
    /**
     * 根据检查项id查询检查项
     * @return
     */
    CheckItem findById(Integer checkItemId);
    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    void edit(CheckItem checkItem);
}
