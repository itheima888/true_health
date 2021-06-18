package com.itheima.test;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ITextTest {
    @Test
    public void testText() throws  Exception {
        Document document= new Document();
        PdfWriter.getInstance(document,new FileOutputStream("C:\\Users\\Administrator\\Desktop\\abc.pdf"));
        document.open();
        //使用JasperReport Studio工具选择字体即可
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        document.add(new Paragraph("黑马程序员 Hello World! 传智播客",new Font(bfChinese)));//往pdf写入内容
        document.close();
    }
}
