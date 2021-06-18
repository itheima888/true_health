package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量预约设置----将excel数据解析并写入数据库t_ordersetting表中
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile excelFile){

        try {
            //1.通过POIUtil工具类解析excel
            List<String[]> listStr = POIUtils.readExcel(excelFile);
            //2.将List<String[]转为List<OrderSetting>
            if(listStr != null && listStr.size()>0){
                List<OrderSetting> orderSettingList = new ArrayList<>();
                for (String[] string : listStr) {
                    OrderSetting orderSetting = new OrderSetting();
                    //遍历每一行数据 放到OrderSetting对象
                    orderSetting.setOrderDate(new Date(string[0]));//预约日期
                    orderSetting.setNumber(Integer.parseInt(string[1]));//可预约 人数
                    //将orderSetting对象 放到List集合中
                    orderSettingList.add(orderSetting);
                }
                if(orderSettingList != null && orderSettingList.size()>0){
                    //3.调用服务保存到数据库
                    orderSettingService.batchAdd(orderSettingList);
                }
            }
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    /**
     * 根据年月查询预约设置数据
     */
    @RequestMapping(value = "/getOSByYearMonth",method =RequestMethod.GET )
    public Result getOSByYearMonth(String yearmonth){


        try {
            List<Map> mapList = orderSettingService.getOSByYearMonth(yearmonth);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);

        }
    }

    /**
     * 单个预约设置
     */
    @RequestMapping(value = "/editNumberByOrderDate",method = RequestMethod.POST)
    public Result editNumberByOrderDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByOrderDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);

        }
    }
}
