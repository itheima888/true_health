package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员服务实现类
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号码查询会员信息
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 自动注册会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
    /**
     * 会员折线图
     */
    @Override
    public Map getMemberReport() {
        //定义map返回结果
        Map map = new HashMap();
        // 1.months List<String>
        Calendar calendar =Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//往前推12个月
        List<String> months = new ArrayList<>();
        for (int i = 1;i<=12;i++){
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            calendar.add(Calendar.MONTH,1);//每次+1个月
        }
        map.put("months",months);
        // 2.memberCount List<Interger>
        List<Integer> memberCount = new ArrayList<>();
        //select count(*) from t_member where regTime <='2020-01-31'
        for (String month : months) {
            String yearMonth = month+"-31";
            Integer mc = memberDao.findMemberCountBeforeDate(yearMonth);
            memberCount.add(mc);
        }
        map.put("memberCount",memberCount);
        return map;
    }


}
