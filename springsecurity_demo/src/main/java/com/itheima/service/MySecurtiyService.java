package com.itheima.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 认证授权类
 *
 * 认证：从数据库查询出密码，密码交给认证管理器对比
 */
@Component
public class MySecurtiyService implements UserDetailsService {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //模拟数据库中的用户数据
    public static Map<String, com.itheima.pojo.User> map = new HashMap<String, com.itheima.pojo.User>();

    static {
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(encoder.encode("admin"));

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("zhangsan");
        user2.setPassword(encoder.encode("123"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }

    
    /**
     * 根据用户名加载用户对象
     * @param username 页面输入的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象(模拟数据库查询)
        com.itheima.pojo.User user = map.get(username);
        //2.用户对象如果为空，说明账号不存在
        if(user == null){
            return null;
        }
        //3.用户对象不为空，获取数据库的密码
        //String password = "{noop}"+user.getPassword();
        String password = user.getPassword();
        //4.将数据库获取的密码xxx返回给认证管理器即可
        //String username, String password, Collection<? extends GrantedAuthority> authorities
        List<GrantedAuthority> list =new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//授权
        list.add(new SimpleGrantedAuthority("add"));//授权
        list.add(new SimpleGrantedAuthority("ROLE_ABC"));//授权
        return new User(username,password,list);
    }
}
