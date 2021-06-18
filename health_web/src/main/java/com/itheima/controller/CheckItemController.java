package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项控制层
 */
@RestController //@Controller + @ResponseBody
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有检查项
     * @return
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //  查询所有检查项
    @RequestMapping(value = "/findAll")
    public Result findAlls(){
        // 使用业务层中的接口
        List<CheckItem> all = checkItemService.findAll();
        if(!CollectionUtils.isEmpty(all)){
              return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,all);

        }else {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
    //新增检查项
   // @PostMapping("/add")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
   public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
            return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }

    }
 /*   *//**
     * 分页查询
     * @param queryPageBean
     * @return
     */
//    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
//    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){// QueryPageBean类是封装了前段传进来的参数
//        return checkItemService.findPage(queryPageBean.getCurrentPage(),// 类名.属性
//                queryPageBean.getPageSize(),   //类名.属性
//                queryPageBean.getQueryString()); //类名.属性
//
//    }
//
    //分页查询
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPages(@RequestBody QueryPageBean queryPageBean){
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();
        //  调用业务层中的数据
        return checkItemService.findPage(currentPage, pageSize, queryString);
    }


    /**
     * 删除检查项
     * @return
     */


    @RequestMapping(value = "delete",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer checkItemId){
        try {
            checkItemService.delete(checkItemId);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
//        return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);

    }

    /**
     * 根据检查项id查询检查项
     * @return
     */
  /*  @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer checkItemId){
        try {
            CheckItem checkItem = checkItemService.findById(checkItemId);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }*/
  @RequestMapping(value = "/findById",method = RequestMethod.GET)
  public Result findById(Integer checkItemId){
      try {
          checkItemService.findById(checkItemId);
          return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
  }

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
   /* @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }*/
   @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem){
       try {
           checkItemService.edit(checkItem);
           return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
   }

}
