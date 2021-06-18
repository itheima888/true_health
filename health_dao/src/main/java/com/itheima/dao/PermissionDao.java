package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionDao {

    /**
     * 通过角色id查询关联的权限集合
     * @param roleId
     * @return
     */
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
