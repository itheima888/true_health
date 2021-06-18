package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.Map;

/**
 * 会员服务接口
 */
public interface MemberService {
    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    public Member findByTelephone(String telephone);

    /**
     * 自动注册会员
     * @param member
     */
    void add(Member member);

    /**
     * 会员折线图
     */
    Map getMemberReport();

}
