package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 体检预约控制层
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    //@Autowired
    //private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 接收预约请求
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submitOrder(@RequestBody Map map) {
        //{"setmealId":"12","sex":"2","orderDate":"2020-08-13","name":"张三",
        //        "telephone":"15572184803","validateCode":"123445","idCard":"110101199003076317"}
        //获取用户输入的验证码  跟redis验证码对比
        Result result = null;
        try {
            String validateCode = (String) map.get("validateCode");
            String telephone = (String) map.get("telephone");
            //获取redis用户验证码
           // String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
            String redisCode =  (String)redisTemplate.opsForValue().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
            //校验验证码
            if (StringUtils.isEmpty(redisCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //调用业务层业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            return orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SYSTEM_FAIL);
        }
    }

    /**
     * 成功页面展示数据
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);

        }
    }

    /**
     * 导出体检预约信息
     */
    @RequestMapping(value = "/exportSetmealInfo", method = RequestMethod.GET)
    public Result exportSetmealInfo(Integer id, HttpServletResponse response) {
        try {
            //1.查询数据预约成功信息（修改返回套餐id）
            Map map = orderService.findById(id);
            Integer setmealId = (Integer) map.get("id");
            //2.根据套餐id查询检查组检查项数据
            Setmeal setmeal = setmealService.findById(setmealId);
            //3.创建文档Document-以输出流方式下载本地-创建Table表格-将Table表格加入文档（Document）-资源关闭
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // 设置表格字体
            BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
            document.add(new Paragraph("体检人：" + (String) map.get("member"), font));
            document.add(new Paragraph("体检套餐：" + (String) map.get("setmeal"), font));
            document.add(new Paragraph("体检日期：" + (String) map.get("orderDate").toString(), font));
            document.add(new Paragraph("预约类型：" + (String) map.get("orderType"), font));
            //Table对象
            Table table = new Table(3);

            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(234, 255, 75)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            //table.setSpacing(5);//设置表格上下的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式


            table.addCell(buildCell("项目名称", font));
            table.addCell(buildCell("项目内容", font));
            table.addCell(buildCell("项目解读", font));

            //往table表格写入数据
            List<CheckGroup> checkGroups = setmeal.getCheckGroups();
            if (checkGroups != null && checkGroups.size() > 0) {
                for (CheckGroup checkGroup : checkGroups) {
                    //项目名称
                    table.addCell(buildCell(checkGroup.getName(), font));
                    //循环检查项
                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    StringBuffer sb = new StringBuffer();
                    if (checkItems != null && checkItems.size() > 0) {
                        for (CheckItem checkItem : checkItems) {
                            sb.append(checkItem.getName() + " ");
                        }
                    }
                    //项目内容
                    table.addCell(buildCell(sb.toString(), font));
                    //项目解读
                    table.addCell(buildCell(checkGroup.getRemark(), font));
                }
            }
            //将table表格添加到document文档中
            document.add(table);
            //设置文件类型
            response.setContentType("application/pdf");
            //设置响应头 文件名称 name:固定的 区分大小写  attachment：附件 filename：文件名
            response.setHeader("content-Disposition", "attachment;filename=hellworld.pdf");
            document.close();
            return null;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.SYSTEM_FAIL);
    }


    // 传递内容和字体样式，生成单元格
    private Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
