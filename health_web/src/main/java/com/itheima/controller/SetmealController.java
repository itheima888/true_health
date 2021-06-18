package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐控制层
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 文件上传
     * 方式一：MultipartFile imgFile
     * 方式二：@RequestParam("imgFile") MultipartFile 可以随便定义
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile imgFile){
        try {
            //1.获取到文件原始名称
            String oldFileName = imgFile.getOriginalFilename();
            System.out.println(oldFileName);
            //上传七牛云
            //2.获取文件名称后缀
            String suffix = oldFileName.substring(oldFileName.indexOf("."));
            //3.生成新的文件名称  并文件名称拼接
            String newFileName = UUID.randomUUID().toString() + suffix;
            //4.上传七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newFileName);

            //记录图片上传成功
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            //5.将最新的文件名称返回页面
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 新增套餐
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setmealService.add(setmeal,checkGroupIds);
            //记录图片上传成功
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    /**
     * 套餐分页
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
    }

    /**
     * 查询套餐
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer setmealId){
        try {
            Setmeal setmeal= setmealService.findById(setmealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id查询关联的检查组ids
     */
    @RequestMapping(value = "/findGroupIdsBySetmealId",method = RequestMethod.GET)
    public List<Integer> findGroupIdsBySetmealId(Integer setmealId){
        return setmealService.findGroupIdsBySetmealId(setmealId);
    }



    /**
     * 编辑套餐
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setmealService.edit(setmeal,checkGroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(Integer setmealId){
        try {
            setmealService.delete(setmealId);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

}
