package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map; /**
 * 检查组接口
 */
public interface CheckGroupDao {
    /**
     * 往检查组插入数据
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 往中间表插入数据
     * @param map
     */
    void setCheckGroupAndCheckItem(Map map);

    /**
     * 检查组分页
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);

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
     * 根据检查组id更新检查组
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组id删除中间表关系
     * @param id
     */
    void deletRsByGroupId(Integer id);

    /**
     * 根据检查组id查询套餐检查组中间表
     * @param checkGroupId
     * @return
     */
    int findCountBySetmealAndCheckGroup(Integer checkGroupId);

    /**
     * 根据检查组id查询检查组检查项中间表
     * @param checkGroupId
     * @return
     */
    int findCountByCheckItemAndCheckGroup(Integer checkGroupId);

    /**
     * 删除检查组
     * @param checkGroupId
     */
    void deleteByCheckGroupId(Integer checkGroupId);
    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();


    /**
     * 根据套餐id查询检查组列表
     * @param setmealId
     * @return
     */
    List<CheckGroup> findCheckGroupListById(Integer setmealId);
}
