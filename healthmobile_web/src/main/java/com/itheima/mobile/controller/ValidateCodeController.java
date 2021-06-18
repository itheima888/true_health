package com.itheima.mobile.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 专门负责发送验证码
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    //@Autowired
    //private JedisPool jedisPool;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 体检预约-验证码
     * @param telephone
     * @return
     */
    /*@RequestMapping(value = "/send4Order",method = RequestMethod.POST)
    public Result send4Order(String telephone){
        try {
            //1.生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //2.调用短信接口发送验证码
            if(false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
            }
            System.out.println("telephone::::"+telephone+"::::code::::"+code);
            //3.将验证码 手机号存入redis  后续验证 5分钟过期
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,code);
            //4.返回结果
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }*/
    @RequestMapping(value = "/send4Order",method = RequestMethod.POST)
    public Result send4Order(String telephone){
        try {
            getCode(telephone,RedisMessageConstant.SENDTYPE_ORDER);
            //4.返回结果
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


    /**
     * 手机号码快速登录-获取验证码
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/send4Login",method = RequestMethod.POST)
    public Result send4Login(String telephone){
        try {
            getCode(telephone,RedisMessageConstant.SENDTYPE_LOGIN);
            //4.返回结果
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 发送验证码公共方法
     * @param telephone
     * @param sendtype
     * @throws ClientException
     */
    private void getCode(String telephone,String sendtype) throws ClientException {
        //1.生成验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        //2.调用短信接口发送验证码
        if(false) {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
        }
        System.out.println("telephone::::"+telephone+"::::code::::"+code);
        //3.将验证码 手机号存入redis  后续验证 5分钟过期
        //jedisPool.getResource().setex(sendtype+"_"+telephone,5*60,code);
        //需要修改value值，每一次都需要设置key
        redisTemplate.opsForValue().set(sendtype+"_"+telephone,code,5, TimeUnit.MINUTES);


        ///需要操作多次key key只需要设置一次
        /*BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(sendtype + "_" + telephone);
        boundValueOperations.set("xxxx");
        boundValueOperations.set("yyyyy");*/

    }
}
