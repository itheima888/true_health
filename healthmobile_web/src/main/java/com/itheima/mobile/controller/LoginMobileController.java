package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 移动端-手机号码快速登录
 */
@RestController
@RequestMapping("/login")
public class LoginMobileController {

    //@Autowired
    //private JedisPool jedisPool;
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private MemberService memberService;
    /**
     * 登录请求
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //获取参数手机号码+验证码 {telephone: "13331112221", validateCode: "111111"}
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        //校验验证码
        if(!StringUtils.isEmpty(telephone) && !StringUtils.isEmpty(validateCode)){
            //获取redis的验证码
            //String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
            String redisCode = (String)redisTemplate.opsForValue().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
            if(!validateCode.equals(redisCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //验证码验证通过
            //校验通过先查询是否会员 根据手机号码查询t_member表
            Member member = memberService.findByTelephone(telephone);
            //不是自动注册会员
            if(member == null){
                member = new Member();
                member.setRegTime(new Date());//注册时间
                member.setPhoneNumber(telephone);//手机号码
                memberService.add(member);
            }
            //将会员信息写入cookie中
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//所有页面都能使用cookie
            cookie.setMaxAge(60*60*24*30);//1个月
            response.addCookie(cookie);
            //返回成功信息给页面
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }
        return new Result(false, MessageConstant.SYSTEM_ERROR);
    }
}
