package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User findUserByUserName(String username);

    /**
     * 根据用户id查询用户权限信息
     * @param id
     * @return
     */
    User findByUserId(Integer id);
}
