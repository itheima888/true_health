package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务实现类
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class  implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        //获取参数
        String orderDate = (String)map.get("orderDate");//获取预约日期
        String telephone = (String)map.get("telephone");//获取手机号码
        String name = (String)map.get("name");//获取姓名
        String sex = (String)map.get("sex");//获取性别
        String idCard = (String)map.get("idCard");//获取身份证
        String orderType = (String)map.get("orderType");//获取预约类型
        String setmealId = (String)map.get("setmealId");//获取套餐id
        //{"setmealId":"12","sex":"2","orderDate":"2020-08-13","name":"张三",
          //      "telephone":"15572184803","validateCode":"123445","idCard":"110101199003076317"}
        //1.判断当前日期 是否可以预约 (1.根据预约日期查询预约表是否为空)
        //将String类型的date转为Date类型
        Date orderDateNew = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDateNew);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.判断当前日期是否约满
        if(orderSetting.getNumber() <= orderSetting.getReservations()){
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.判断是否会员 根据手机号码 查询 t_member
        Member member = memberDao.findByTelephone(telephone);
        boolean flag = true; //  默认true是会员的
        if(member == null){
            flag = false;    //  如果是会员，就不走这里的了
            //自动注册
            member = new Member();
            member.setName(name);//姓名
            member.setSex(sex);//性别
            member.setIdCard(idCard);//身份证
            member.setRegTime(new Date());//注册时间
            member.setPhoneNumber(telephone);//手机号码
            memberDao.add(member);
        }

        //默认认为是会员
        if(flag) {
            //4.判断是否重复预约 会员id + 预约时间+套餐id
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(orderDateNew);//预约日期
            order.setSetmealId(Integer.parseInt(setmealId));
            //  查询预约数据    如果里面有数据了   就提示已经预约了啊啊啊啊啊
            List<Order> listOrder = orderDao.findByCondition(order);
            if (listOrder != null && listOrder.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //5.往预约表插入记录
        Order order = new Order();
        order.setMemberId(member.getId());//会员id
        order.setOrderDate(orderDateNew);//预约日期
        order.setOrderType(orderType);//预约类型 微信预约 、电话预约
        order.setOrderStatus(Order.ORDERSTATUS_NO);//默认状态未到诊
        order.setSetmealId(Integer.parseInt(setmealId));//套餐id
        orderDao.add(order);

        //6.更新预约设置已预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        // 7.发送预约通知短信给用户
        if(false){
            SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);//orderDate:2020-08-01
            //SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, orderDate);
        }
        System.out.println("发送预约通知短信成功了。。。");
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }
    /**
     * 成功页面展示数据
     */
    @Override
    public Map findById(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
