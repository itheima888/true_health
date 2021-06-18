package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐服务接口
 */
public interface SetmealService {
    /**
     * 新增套餐
     */
    void add(Setmeal setmeal, Integer[] checkGroupIds);
    /**
     * 套餐分页
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 查询套餐
     */
    Setmeal findById(Integer setmealId);
    /**
     * 根据套餐id查询关联的检查组ids
     */
    List<Integer> findGroupIdsBySetmealId(Integer setmealId);
    /**
     * 编辑套餐
     */
    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    /**
     * 删除套餐
     */
    void delete(Integer setmealId);
    /**
     * 查询套餐列表
     */
    List<Setmeal> getSetmeal();

    /**
     * 套餐饼图
     */
    Map getSetmealReport();
}
