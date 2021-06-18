package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map; /**
 * 套餐服务接口
 */
public interface SetmealDao {
    /**
     * 先往套餐表插入数据
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 往检查组套餐中间表
     * @param map
     */
    void setCheckGroupAndSetmeal(Map map);
    /**
     * 套餐分页
     */
    Page<Setmeal> selectByCondition(String queryString);
    /**
     * 查询套餐
     */
    Setmeal findById(Integer setmealId);

    /**
     * 根据套餐id查询关联的检查组ids
     */
    List<Integer> findGroupIdsBySetmealId(Integer setmealId);

    /**
     * 根据套餐id修改套餐数据
     * @param setmeal
     */
    void edit(Setmeal setmeal);

    /**
     * 根据套餐id删除套餐检查组中间表关系数据
     * @param id
     */
    void deleteRsBySetmealId(Integer id);

    /**
     * 根据套餐id查询套餐检查组中间表
     * @param setmealId
     * @return
     */
    int findCountBySetmealId(Integer setmealId);

    /**
     * 关系不存在直接直接删除
     * @param setmealId
     */
    void delete(Integer setmealId);

    /**
     * 查询套餐列表
     */
    List<Setmeal> getSetmeal();

    /**
     * 获取套餐名称以及预约次数
     * @return
     */
    List<Map<String,Object>> findSetmealNamesCount();
}
