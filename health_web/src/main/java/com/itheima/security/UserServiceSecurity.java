package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自定义认证授权类
 */
@Component
public class UserServiceSecurity implements UserDetailsService {

    @Reference
    private UserService userService;

    /**
     * 根据用户名加载用户对象 ---> 交给认证管理器
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询数据库中用户对象
        com.itheima.pojo.User user =  userService.findUserByUserName(username);
        //2.根据返回用户对象是否为空 ，如果为空return null
        if(user == null){
            return null;
        }
        //3.如果不为 获取数据库用户的密码（加密后的密文）
        String dbPassword = user.getPassword();
        List<GrantedAuthority> list = new ArrayList<>();
        //4.根据用户id查询用户权限信息（角色表、权限表 关键字）
        com.itheima.pojo.User userKeyWord = userService.findByUserId(user.getId());
        if(userKeyWord != null){
            Set<Role> roles = userKeyWord.getRoles();
            if(roles != null && roles.size()>0){
                for (Role role : roles) {
                    String roleKeyword = role.getKeyword();
                    list.add(new SimpleGrantedAuthority(roleKeyword));//角色表关键字

                    Set<Permission> permissions = role.getPermissions();
                    if(permissions != null && permissions.size()>0){
                        for (Permission permission : permissions) {
                            String permissionKeyword = permission.getKeyword();
                            list.add(new SimpleGrantedAuthority(permissionKeyword));//角色表关键字
                        }
                    }
                }
            }
        }
        //5.返回用户对象给框架 （认证成功后所有的认证信息 存入SecurityContext容器对象中）
        return new User(username,dbPassword,list);
    }
}
