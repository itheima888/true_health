package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置
 */
public interface OrderSettingService {
    /**
     * 批量预约设置方法
     * @param orderSettingList
     */
    void batchAdd(List<OrderSetting> orderSettingList);
    /**
     * 根据年月查询预约设置数据
     */
    List<Map> getOSByYearMonth(String yearmonth);
    /**
     * 单个预约设置
     */
    void editNumberByOrderDate(OrderSetting orderSetting);


}
