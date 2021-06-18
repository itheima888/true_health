package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检查项持久层接口
 */
public interface CheckItemDao {
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
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据检查项id查询检查项检查组中间表
     * @param checkItemId
     * @return
     */
    int selectCountByCheckItemId(Integer checkItemId);


    /**
     * 如果记录不存在 则直接删除
     * @param checkItemId
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

    /**
     * 根据检查组id关联查询检查项列表
     * @param checkGroupId
     * @return
     */
    List<CheckItem> findCheckItemListById(Integer checkGroupId);
}
