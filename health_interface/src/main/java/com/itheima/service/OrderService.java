package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * 体检预约服务接口
 */
public interface OrderService {
    /**
     * 体检预约
     * @param map
     * @return
     */
    Result submitOrder(Map map) throws Exception;
    /**
     * 成功页面展示数据
     */
    Map findById(Integer id);
}
