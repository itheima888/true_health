package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * 预约设置持久层接口
 */
public interface OrderSettingDao {
    /**
     * 根据预约日期到t_ordersetting查询数据是否存在
     * @param orderDate
     * @return
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 根据预约日期修改预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 新增预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据年月查询预约设置数据
     */
    List<OrderSetting> getOSByYearMonth(Map map);

    /**
     * 判断当前日期 是否可以预约
     * @param orderDateNew
     * @return
     */
    OrderSetting findByOrderDate(Date orderDateNew);

    /**
     * 更新预约设置已预约人数+1
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
