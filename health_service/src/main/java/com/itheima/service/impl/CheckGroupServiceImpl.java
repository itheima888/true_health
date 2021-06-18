package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务实现类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * @param checkGroup
     */
    @Override
    public void add(CheckGroup checkGroup,Integer[] checkItemIds) {
        //1.往检查组插入数据
        checkGroupDao.add(checkGroup);
        //却出检查组id
        Integer groupId = checkGroup.getId();
        //循坏遍历往中间表中插入数据
        setCheckGroupAndCheckItem(groupId,checkItemIds);
     /*   //1.往检查组插入数据
        checkGroupDao.add(checkGroup);
        //2.获取检查组id
        Integer groupId = checkGroup.getId();
        System.out.println("****************");
        //3.循环遍历往中间表插入数据(抽取方法 编辑也能使用)
        setCheckGroupAndCheckItem(groupId,checkItemIds);*/
    }
    /**
     * 检查组分页
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);//设置分页参数
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }
    /**
     * 根据检查组id查询检查组对象
     * @return
     */
    @Override
    public CheckGroup findById(Integer groupId) {
        return checkGroupDao.findById(groupId);
    }
    /**
     * 根据检查组id 查询检查项ids
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByGroupId(Integer groupId) {
        return checkGroupDao.findCheckItemIdsByGroupId(groupId);
    }
    /**
     * 检查组编辑
     * @return
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //4.	根据检查组id更新检查组
        checkGroupDao.edit(checkGroup);
        //5.	根据检查组id删除中间表关系
        checkGroupDao.deletRsByGroupId(checkGroup.getId());
        //6.	重新建立中间表关系（ok）
        setCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);
    }
    /**
     * 检查组删除
     * @return
     */
    @Override
    public void delete(Integer checkGroupId) {
        //1.	根据检查组id查询套餐检查组中间表，如果关系存在，不允许删除
        int count = checkGroupDao.findCountBySetmealAndCheckGroup(checkGroupId);
        if(count>0){
            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_CHECKGROUP_FAIL);
        }
        //2.	根据检查组id查询检查组检查项中间表，如果关系存在，不允许删除
        int count2 = checkGroupDao.findCountByCheckItemAndCheckGroup(checkGroupId);
        if(count2>0){
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_CHECKGROUP_FAIL);
        }
        //3.	关系不存在则可以删除
        checkGroupDao.deleteByCheckGroupId(checkGroupId);
    }
    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 公共方法 - 往中间表插入数据
     * @param groupId
     * @param checkItemIds
     */
    private void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkItemIds) {
        //如果检查项存在
        if(checkItemIds != null && checkItemIds.length>0){
            //遍历
            for (Integer checkItemId : checkItemIds) {
                //定义map出入
                Map map = new HashMap<>();
                map.put("checkItemId",checkItemId);
                map.put("groupId",groupId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
       /* if(checkItemIds != null && checkItemIds.length>0){
            for (Integer checkItemId : checkItemIds) {
                //checkItemId:检查项id  groupId:检查组id
                //定义map传入
                Map map = new HashMap<>();
                map.put("checkItemId",checkItemId);
                map.put("groupId",groupId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }*/
    }
}
