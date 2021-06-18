package com.itheima.job;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 将当前类注册到spring容器中
 */
//@Component
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    /**
     * 处理垃圾图片
     */
    public void clearImg(){
        //集合1 – 集合2 = 差集  (sdiff())                套餐图片所有图片名称           -           套餐图片保存在数据库中的图片名称
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        System.out.println("任务被执行了。。。"+set.size());
        if(set != null && set.size()>0){
            for (String imgFileName : set) {
                //调用七牛云删除图片
                QiniuUtils.deleteFileFromQiniu(imgFileName);
                //将redis中多余记录也要删除（集合1）
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgFileName);
                //从RedisConstant.SETMEAL_PIC_RESOURCES将imgFileName删除
            }
        }

        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        System.out.println("任务被执行了---"+ sdiff.size());
        if(sdiff != null && sdiff.size()>0){
            for (String s : sdiff) {
                //  调用七牛云删除图片
                QiniuUtils.deleteFileFromQiniu(s);
                //  之后将redis缓存中的图片删除掉
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,s);
            }
        }
    }
}
