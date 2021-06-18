package com.itheima;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestSpringSecurity {

    //@Test
    public void testSS(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //相当于注册往数据库用户表 给密码加密
        String dbPassword = encoder.encode("123456");
        //$2a$10$HOmaFv.ZIYZBBibmxQ/8BOQzpKBZDYJuGr9rQfI96E/PHdNdc8Kju
        //$2a$10$DNOq1hY4jc1dMTeOP3wPBOJ1S6.zZQYPq9b9tJ1Xtc5kSf79l206q
        System.out.println(dbPassword);

        boolean flag = encoder.matches("123456", "$2a$10$DNOq1hY4jc1dMTeOP3wPBOJ1S6.zZQYPq9b9tJ1Xtc5kSf79l206q");
        System.out.println(flag);
    }
}
