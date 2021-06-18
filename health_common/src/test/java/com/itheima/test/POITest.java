package com.itheima.test;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class POITest {

    /**
     * 方式一：读取excel
     * @throws Exception
     */
    //@Test
    public void readExcel() throws Exception {
        //1.获取工作薄
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File("C:\\Users\\Administrator\\Desktop\\read.xlsx"));
        //2.获取工作表
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //3.遍历工作表
        for (Row row : sheet) {
            //4.每一列
            for (Cell cell : row) {
                //5.获取数据，输出
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("*********************************************");
        }
        //6.关闭
        xssfWorkbook.close();

    }

    /**
     * 方式二：读取excel(这种方式比较灵活)
     * @throws Exception
     */
    //@Test
    public void readExcel2() throws Exception {
        //1.获取工作薄
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File("C:\\Users\\Administrator\\Desktop\\read.xlsx"));
        //2.获取工作表
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //3.获取最后一行行号
        int lastRowNum = sheet.getLastRowNum();//下标
      //  System.out.println("最后一行下标"+lastRowNum);
        for (int i =0;i<=lastRowNum;i++ ){
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
           // System.out.println("最后一列列号"+lastCellNum);
            for (int j=0;j<lastCellNum;j++){
                System.out.println(row.getCell(j).getStringCellValue());
            }
            System.out.println("**********************************");
        }

    }

    /**
     * 往excel中写入数据
     */
    //@Test
    public void createExcel() throws IOException {
        //1.创建空excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //2.创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet("用户信息");
        //3.创建标题行
            XSSFRow titleRow = sheet.createRow(0);
        //设置数据
        titleRow.createCell(0).setCellValue("编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");

        //4.数据行 List<T>
        XSSFRow dataRow = sheet.createRow(1);
        //设置数据
        dataRow.createCell(0).setCellValue("001");
        dataRow.createCell(1).setCellValue("老王");
        dataRow.createCell(2).setCellValue("18");

        //5.将内存中excel 通过输出流方式写到磁盘中
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\aaaaa.xlsx"));
        xssfWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.close();
    }

}
