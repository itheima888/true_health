package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 报表模块
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员折线图
     */
    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    public Result getMemberReport(){
        //调用服务获取会员折线图数据
        try {
            Map map = memberService.getMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 套餐饼图
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    public Result getSetmealReport(){
        try {
            Map map = setmealService.getSetmealReport();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计报表
     */
    @RequestMapping(value = "/getBusinessReportData",method = RequestMethod.GET)
    public Result getBusinessReportData(){
        try {
            Map map = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计报表导出-poi
     */
   /* @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try {
            //1.获取模板 File.separator linux windows
            String reportTemplate = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //2.POI获取Excel对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(reportTemplate)));

            //3.获取Excel所需的数据
            Map result = reportService.getBusinessReportData();
            String reportDate = (String)result.get("reportDate");
            Integer todayNewMember = (Integer)result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> mapList = (List<Map>)result.get("hotSetmeal");

            //4.为Excel填充数据
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);//第三行
            row.getCell(5).setCellValue(reportDate);//第三行第六列赋值

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //从12行开始写数据
            int rownum = 12;
            if(mapList != null && mapList.size()>0){
                for (Map map : mapList) {
                    //写热门套餐数据
                    row = sheet.getRow(rownum);
                    row.getCell(4).setCellValue((String)map.get("name"));//套餐名称
                    row.getCell(5).setCellValue((Long)map.get("setmeal_count"));//预约数量
                    BigDecimal proportion = (BigDecimal) map.get("proportion");
                    row.getCell(6).setCellValue(proportion.doubleValue());//占比
                    row.getCell(7).setCellValue((String)map.get("remark"));//备注
                    rownum++;
                }
            }


            //5.以输出流形式导出excel
            OutputStream outputStream = response.getOutputStream();
            //设置文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置响应头 文件名称 name:固定的 区分大小写  attachment：附件 filename：文件名
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);

            //6.资源关闭
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_FAIL);
        }
    }*/

    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try {
            //1.获取模板 File.separator linux windows
            String reportTemplate = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //2.POI获取Excel对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(reportTemplate)));

            //3.获取Excel所需的数据
            Map result = reportService.getBusinessReportData();
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook, result);
            //5.以输出流形式导出excel
            OutputStream outputStream = response.getOutputStream();
            //设置文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置响应头 文件名称 name:固定的 区分大小写  attachment：附件 filename：文件名
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);

            //6.资源关闭
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_FAIL);
        }
    }


    /**
     * 导出运营数据统计报表PDF
     */
    @RequestMapping(value = "/exportBusinessPDFReport",method = RequestMethod.GET)
    public Result exportBusinessPDFReport(HttpServletRequest request,HttpServletResponse response){

        try {
            //1.获取PDF所需数据
            Map result = reportService.getBusinessReportData();
            List<Map> mapList = (List<Map>)result.get("hotSetmeal");//套餐列表数据
            //2.获取模板路径
            String jrxml = request.getSession().getServletContext().getRealPath("template") + File.separator + "reportbusiness.jrxml";
            String jasper = request.getSession().getServletContext().getRealPath("template") + File.separator + "reportbusiness.jasper";
            //3.编译模板
            JasperCompileManager.compileReportToFile(jrxml,jasper);
            //4.填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, result, new JRBeanCollectionDataSource(mapList));
            //5.导出PDF（outputstream）
            //设置文件类型
            response.setContentType("application/pdf");
            //设置响应头 文件名称 name:固定的 区分大小写  attachment：附件 filename：文件名
            response.setHeader("content-Disposition", "attachment;filename=bussiness.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
            //6.释放资源
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_FAIL);
        }
    }
}
