package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

public interface RoleDao {
    /**
     * 通过用户id查询关联的角色集合
     */
    Set<Role> findRolesByUserId(int userId);
}
