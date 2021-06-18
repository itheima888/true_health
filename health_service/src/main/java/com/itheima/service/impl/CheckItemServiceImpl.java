package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务实现类
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;
    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    //增加检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询

    /**
     * 新增检查项
     * @return
     */

    /**
     * 分页查询
     * @return
     */
   /* @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第三步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第四步：为哪个sql语句分页（没有limit关键字）
        Page<CheckItem> checkItemPage =  checkItemDao.selectByCondition(queryString); //select * from table where 条件=xxx
        //第五步：获取结果
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }*/
   //分页查询
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //为那个sql语句分页（没有limit参数）
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        //获取结果
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }
    /**
     * 删除检查项
     * @return
     */
    @Override
    public void delete(Integer checkItemId) {
        //1.根据检查项id查询检查项检查组中间表
        int count = checkItemDao.selectCountByCheckItemId(checkItemId);
        //2.如果记录存在则往上抛出异常
        if(count>0){
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_CHECKGROUP_FAIL);
        }
        //3.如果记录不存在 则直接删除
        checkItemDao.delete(checkItemId);
    }
    /**
     * 根据检查项id查询检查项
     * @return
     */
    @Override
    public CheckItem findById(Integer checkItemId) {
        return checkItemDao.findById(checkItemId);
    }
    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
}
