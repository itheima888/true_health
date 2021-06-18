package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组接口
 */
public interface CheckGroupService {
    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup,Integer[] checkItemIds);

    /**
     * 检查组分页
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 根据检查组id查询检查组对象
     * @return
     */
    CheckGroup findById(Integer groupId);
    /**
     * 根据检查组id 查询检查项ids
     * @return
     */
    List<Integer> findCheckItemIdsByGroupId(Integer groupId);

    /**
     * 检查组编辑
     * @return
     */
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);
    /**
     * 检查组删除
     * @return
     */
    void delete(Integer checkGroupId);
    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}
