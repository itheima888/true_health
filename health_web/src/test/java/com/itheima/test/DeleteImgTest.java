package com.itheima.test;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 删除垃圾图片测试类
 */
//@ContextConfiguration(locations = "classpath:spring-redis.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
public class DeleteImgTest {

    @Autowired
    private JedisPool jedisPool;

    //@Test
    public void deleteImg(){
        //集合1 – 集合2 = 差集  (sdiff())
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null && set.size()>0){
            for (String imgFileName : set) {
                //调用七牛云删除图片
                QiniuUtils.deleteFileFromQiniu(imgFileName);
                //将redis中多余记录也要删除（集合1）
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgFileName);
                //从RedisConstant.SETMEAL_PIC_RESOURCES将imgFileName删除
            }
        }


    }
}
