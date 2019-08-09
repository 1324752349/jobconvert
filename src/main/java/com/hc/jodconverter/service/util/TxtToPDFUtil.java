package com.hc.jodconverter.service.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class TxtToPDFUtil {
    private static final String FONT = "C:\\Windows\\Fonts\\simhei.ttf";
    public static void txtTopdf(MultipartFile file, File pdfFile) throws DocumentException, IOException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(pdfFile);
        PdfWriter.getInstance(document, os);
        document.open();
        //方法一：使用Windows系统字体(TrueType)
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont);
        File savedFile = new File("D:\\tmp\\" + file.getName());
        file.transferTo(savedFile);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(savedFile), "GBK");
        BufferedReader bufferedReader = new BufferedReader(isr);
        String str = "";
        while ((str = bufferedReader.readLine()) != null) {
            document.add(new Paragraph(str, font));
        }
        document.close();
        os.close();
        isr.close();
        //将临时txt文件删除
        if (savedFile.exists() && savedFile.isFile()) {
            savedFile.delete();
        }
    }

    public static void txtTopdf(String txtPath, String pdf) throws DocumentException, IOException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(new File(pdf));
        PdfWriter.getInstance(document, os);
        document.open();
        //方法一：使用Windows系统字体(TrueType)
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont);
        File txtFile = new File(txtPath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(txtFile), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(isr);
        String str = "";
        while ((str = bufferedReader.readLine()) != null) {
            document.add(new Paragraph(str, font));
        }
        document.close();
        os.close();
        isr.close();
        //将临时txt文件删除
        if (txtFile.exists() && txtFile.isFile()) {
            txtFile.delete();
        }
    }

}
