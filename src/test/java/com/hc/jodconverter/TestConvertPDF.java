package com.hc.jodconverter;

import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;
import org.junit.Test;

import java.io.File;


public class TestConvertPDF {
    @Test
    public void convertPDF() throws OfficeException {
       /* LocalOfficeManager manager = LocalOfficeManager.builder().officeHome("/opt/libreoffice6.1").install().build();
        manager.start();
        JodConverter.convert(new File("D:\\测试ceph上传文件2.doc")).to(new File("D:\\test.pdf")).execute();
*/
        File inputFile = new File("D:\\tmp\\中文.txt");
        File outputFile = new File("D:\\tmp\\document.pdf");
        //LocalOfficeManager officeManager = LocalOfficeManager.builder().officeHome("/opt/libreoffice6.1").install().build();
        final LocalOfficeManager officeManager = LocalOfficeManager.install();
        try {
            // Start an office process and connect to the started instance (on port 2002).
            officeManager.start();

            // Convert
            JodConverter
                    .convert(inputFile)
                    .to(outputFile)
                    .execute();
        } finally {
            // Stop the office process
            OfficeUtils.stopQuietly(officeManager);
        }

    }
}
