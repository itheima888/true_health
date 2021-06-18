package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐实现类
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetmealDao setmealDao;

    /**
     * @Value：获取配置文件的值
     * ${key}必须跟配置文件名称要一致
     * String outPutPath 属性名称可以自己定义
     */
    @Value("${out_put_path}")
    private String outPutPath;


    /**
     * 新增套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        //先往套餐表插入数据
        setmealDao.add(setmeal);
        //获取套餐id
        Integer setmealId = setmeal.getId();
        //根据套餐id  和 检查组ids 往中间表插入数据
        setCheckGroupAndSetmeal(setmealId,checkGroupIds);
        //1.新增套餐后，生成套餐列表以及套餐详情页面
        generateMobileStaticHtml();
    }

    /**
     * 1.新增套餐后，生成套餐列表以及套餐详情页面
     */
    private void generateMobileStaticHtml() {
        //查询套餐list数据
        List<Setmeal> setmeal = setmealDao.getSetmeal();
        //2.调用生成套餐列表页面
        generateMobileSetmealListHtml(setmeal);
        //3.调用生成套餐详情页面
        generateMobileSetmealDetailHtml(setmeal);
    }


    /**
     * 2.调用生成套餐列表页面
     */
    private void generateMobileSetmealListHtml(List<Setmeal> setmeal) {
        if(setmeal != null && setmeal.size()>0){
            //模板名称
            String templateName = "mobile_setmeal.ftl";
            Map map = new HashMap();
            map.put("setmealList",setmeal);//key必须要跟模板中的一致
            generateHtml(templateName,map,"m_setmeal.html");
        }
    }

    /**
     * 3.调用生成套餐详情页面
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmeal) {
        if(setmeal != null && setmeal.size()>0) {
            String templateName = "mobile_setmeal_detail.ftl";
            //循环生成套餐详情页面
            for (Setmeal sm : setmeal) {
                Map map = new HashMap();
                Integer setmealId = sm.getId();
                //根据套餐id查询套餐详情数据
                Setmeal rsSM = setmealDao.findById(setmealId);
                map.put("setmeal",rsSM);//key值跟模板中名称要保持一致
                //setmeal_detail_${setmeal.id}.html
                generateHtml(templateName, map, "setmeal_detail_" + setmealId + ".html");
            }
        }
    }

    /**
     * 生成静态页面公共方法
     * @param templateName  模板名称
     * @param map   模板需要的数据
     * @param outHtmlName  输出的文件名
     */
    private void generateHtml(String templateName, Map map, String outHtmlName) {
        Writer writer = null;
        try {
            //获取配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //加载模板对象
            Template template = configuration.getTemplate(templateName);
            //FileWriter：直接操作磁盘 效率低 BufferWriter：操作缓存 效率高 "setmeal_detail_" + setmealId + ".html"
            //拼接文件输出路径+文件名称 File.separator
            String newPathFileName = outPutPath+"/"+outHtmlName;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newPathFileName)));
            //生成静态页面（报错概率比较大）
            template.process(map,writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成静态页面失败。。。。。。");
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 套餐分页
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }
    /**
     * 查询套餐
     */
    @Override
    public Setmeal findById(Integer setmealId) {
        return setmealDao.findById(setmealId);
    }
    /**
     * 根据套餐id查询关联的检查组ids
     */
    @Override
    public List<Integer> findGroupIdsBySetmealId(Integer setmealId) {
        return setmealDao.findGroupIdsBySetmealId(setmealId);
    }
    /**
     * 编辑套餐
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        //根据套餐id修改套餐数据
        setmealDao.edit(setmeal);
        //根据套餐id删除套餐检查组中间表关系数据
        setmealDao.deleteRsBySetmealId(setmeal.getId());
        //根据套餐id+检查组ids往套餐检查组中间表插入
        setCheckGroupAndSetmeal(setmeal.getId(),checkGroupIds);
    }
    /**
     * 删除套餐
     */
    @Override
    public void delete(Integer setmealId) {
        //根据套餐id查询套餐检查组中间表
        int count = setmealDao.findCountBySetmealId(setmealId);
        // 关系存在往上抛异常
        if(count>0){
            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_CHECKGROUP_FAIL);
        }
        //先查询套餐数据 （获取文件名称 去七牛云删除）
        Setmeal setmeal = setmealDao.findById(setmealId);
        //关系不存在直接直接删除（dao）
        setmealDao.delete(setmealId);
        //调用七牛云接口删除图片  注意：先将套餐查询 再删除
        QiniuUtils.deleteFileFromQiniu(setmeal.getImg());

    }

    /**
     * 查询套餐列表
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /**
     * 往检查组套餐中间表
     * @param setmealId
     * @param checkGroupIds
     */
    private void setCheckGroupAndSetmeal(Integer setmealId, Integer[] checkGroupIds) {
        if(checkGroupIds != null && checkGroupIds.length>0){
            for (Integer checkGroupId : checkGroupIds) {
                //checkItemId:检查项id  groupId:检查组id
                //定义map传入
                Map map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("setmealId",setmealId);
                setmealDao.setCheckGroupAndSetmeal(map);
            }
        }
    }

    /**
     * 套餐饼图
     */
    @Override
    public Map getSetmealReport() {
        //定义map返回结果
        Map map = new HashMap();
        //定义List<String>套餐名称
        List<String> setmealNames = new ArrayList<>();

        //1.获取套餐名称以及预约次数
        List<Map<String,Object>> setmealCount = setmealDao.findSetmealNamesCount();
        //2.获取套餐名称
        if(setmealCount != null && setmealCount.size()>0){
            for (Map<String, Object> sc : setmealCount) {
                String name = (String)sc.get("name");
                setmealNames.add(name);
            }
        }
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",setmealCount);
        return map;
    }
}
