package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserDao;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    @Override
    public User findUserByUserName(String username) {
        return userDao.findUserByUserName(username);
    }

    /**
     * 根据用户id查询用户权限信息
     * @param userId
     * @return
     */
    @Override
    public User findByUserId(Integer userId) {
        return userDao.findById(userId);
    }
}
