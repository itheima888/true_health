package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务层
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量预约设置
     *
     * @param orderSettingList
     */
    @Override
    public void batchAdd(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                addOrderSetting(orderSetting);
            }
        }
    }

    /**
     * 抽取公共预约设置方法
     * @param orderSetting
     */
    private void addOrderSetting(OrderSetting orderSetting) {
        //根据预约日期到t_ordersetting查询数据是否存在
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        // 存在则更新
        if (count > 0) {
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //不存在则插入
            orderSettingDao.add(orderSetting);
        }



        //1返回OrderSetting（根据预约日期查询 Ordersetting）

        //2.判断OrderSetting是否为空，如果不为空

        //3.不为空，页面输入的可预约人数(orderSetting,getNumber页面输入的) < 已经预约人数（dbOrderSeting.getReservations） 抛出异常

        //4.为空则直接插入数据

    }

    /**
     * 根据年月查询预约设置数据
     */
    @Override
    public List<Map> getOSByYearMonth(String yearmonth) {

        //拼接起始年月日 2020-8-1 结束年月日 2020-8-31
        String startDate = yearmonth + "-1";
        String endDate = yearmonth + "-31";
        //调用dao 查询数据返回List<OrderSetting>
        Map map = new HashMap();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        List<OrderSetting> orderSettingList = orderSettingDao.getOSByYearMonth(map);
        //将List<OrderSetting>转List<Map>
        List<Map> listMap = new ArrayList<>();
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                Map rsMap = new HashMap();
                //{ date: 1, number: 120, reservations: 1 }
                rsMap.put("date", orderSetting.getOrderDate().getDate());//获取当前日期的几号
                rsMap.put("number", orderSetting.getNumber());//可预约人数
                rsMap.put("reservations", orderSetting.getReservations());//已经预约人数
                listMap.add(rsMap);
            }
        }
        return listMap;



    }

    /**
     * 单个预约设置
     * @param orderSetting
     */
    @Override
    public void editNumberByOrderDate(OrderSetting orderSetting) {
        addOrderSetting(orderSetting);
    }
}
