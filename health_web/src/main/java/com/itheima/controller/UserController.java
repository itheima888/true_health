package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取用户名
     */
    @RequestMapping(value = "/getUserName",method = RequestMethod.GET)
    public Result getUserName(){
        try {
            //SecurityContext:安全容器（认证信息在此容器中）
            //getAuthentication():认证信息
            //.getName()获取用户名
            //.getAuthorities() 权限列表
            //.getPrincipal() User对象
            User  user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
