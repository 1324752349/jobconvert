package com.hc.jodconverter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.fonts.cmaps.*;

import java.io.*;

public class TxtToPDFTest {
    private static final String FONT = "C:\\Windows\\Fonts\\simhei.ttf";
    public static void text2pdf(String text, String pdf) throws DocumentException, IOException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(new File(pdf));
        PdfWriter.getInstance(document, os);
        document.open();
        //方法一：使用Windows系统字体(TrueType)
        //中文字体,解决中文不能显示问题
        BaseFont bfChinese = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);//1 不能省略，省略会报以上错误。
        BaseFont bf = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(text)), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(isr);
        String str = "";
        while ((str = bufferedReader.readLine()) != null) {
            document.add(new Paragraph(str, font));
        }
        document.close();
    }
    public static void main(String[] args) throws Exception {
        String PDFTIMEDIR = "D:\\tmp\\";
        String text = PDFTIMEDIR + "谷歌.txt";
        String pdf = PDFTIMEDIR + "1.txt.pdf";
        text2pdf(text, pdf);
    }


}
