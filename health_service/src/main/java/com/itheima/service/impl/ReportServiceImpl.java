package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Member;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营数据统计报表实现类
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    /**
     * 运营数据统计报表接口
     */
    @Override
    public Map getBusinessReportData() throws Exception {
        //1.定义Map结果
        Map rsMap = new HashMap();
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 获取本周最后一天的日期
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 获取本月最后一天的日期
        String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());


        //2.会员数据
        //获取今日新增会员数量
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);

        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //3.预约数据
        //获取今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);


        // 本周预约数
        Map<String,Object> weekMap = new HashMap<String,Object>();
        weekMap.put("begin",thisWeekMonday);
        weekMap.put("end",thisWeekSunday);
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(weekMap);

        // 本月预约数
        Map<String,Object> monthMap = new HashMap<String,Object>();
        monthMap.put("begin",firstDay4ThisMonth);
        monthMap.put("end",lastDay4ThisMonth);
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(monthMap);

        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);

        // 本周到诊数
        Map<String,Object> weekMap2 = new HashMap<String,Object>();
        weekMap2.put("begin",thisWeekMonday);
        weekMap2.put("end",thisWeekSunday);
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(weekMap2);

        // 本月到诊数
        Map<String,Object> monthMap2 = new HashMap<String,Object>();
        monthMap2.put("begin",firstDay4ThisMonth);
        monthMap2.put("end",lastDay4ThisMonth);
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(monthMap2);

        //4.热门套餐数据
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        rsMap.put("reportDate",reportDate);
        rsMap.put("todayNewMember",todayNewMember);
        rsMap.put("totalMember",totalMember);
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",hotSetmeal);
        return rsMap;
    }
}
