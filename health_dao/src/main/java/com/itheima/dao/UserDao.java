package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * 用户持久层接口
 */
public interface UserDao {
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User findUserByUserName(String username);

    /**
     * 根据用户id查询用户权限信息
     * @param userId
     * @return
     */
    User findById(Integer userId);
}
