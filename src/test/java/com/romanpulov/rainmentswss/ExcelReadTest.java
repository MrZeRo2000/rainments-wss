package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.transform.ExcelReadException;
import com.romanpulov.rainmentswss.transform.ExcelReader;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class ExcelReadTest {
    private static final String TEST_FILE_NAME = "data/Payments.xls";

    static final Logger logger = LoggerFactory.getLogger(ExcelReadTest.class);

    @Test
    void test() {
        File xlsFile = new File(TEST_FILE_NAME);
        Assertions.assertTrue(xlsFile.exists());
    }

    @Test
    void readFile() throws Exception {
        try (InputStream in = new FileInputStream(TEST_FILE_NAME)) {
            Workbook wb = WorkbookFactory.create(in);
            Assertions.assertNotNull(wb);

            Sheet sheet = wb.getSheetAt(0);
            Assertions.assertNotNull(sheet);

            DataFormatter formatter = new DataFormatter();

            for (Row row: sheet) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String cellValue = formatter.formatCellValue(cell);
                    logger.info("Cell 0 value:" + cellValue);
                }

                cell = row.getCell(3);
                if (cell != null) {
                    String cellValue = formatter.formatCellValue(cell);
                    logger.info("Cell 3 value:" + cellValue);
                }
            }
        }
    }

    @Test
    void testReaderBaseLine() throws Exception {
        //no beans
        ExcelReader excelReader = new ExcelReader();

        try (InputStream in = new FileInputStream(TEST_FILE_NAME)) {
            excelReader.setInputStream(in);
            Sheet sheet = excelReader.readDataSheet();
            List<ExcelReader.BaseLine> baseLineList = excelReader.readBaseLineList(sheet);
            Assertions.assertEquals("Вывоз бытовых отходов", baseLineList.get(1).productName);
            Assertions.assertEquals(13, baseLineList.size());
            baseLineList.forEach(baseLine -> logger.info(baseLine.toString()));
        }
    }

    @Test
    void testDateColumnMapping() throws Exception {
        //no beans
        ExcelReader excelReader = new ExcelReader();

        try (InputStream in = new FileInputStream(TEST_FILE_NAME)) {
            excelReader.setInputStream(in);
            Sheet sheet = excelReader.readDataSheet();
            List<ExcelReader.DateColumnMapping> columnMappings = excelReader.readDateColumnMapping(sheet);

            Assertions.assertEquals(LocalDate.of(2016, 10, 1), columnMappings.get(0).periodDate);
            Assertions.assertEquals(LocalDate.of(2020, 2, 1), columnMappings.get(columnMappings.size() - 1).periodDate);
            Assertions.assertEquals(0, columnMappings.size() % 2);
        }

    }


    void testReaderContents() throws Exception {
        //no beans
        ExcelReader excelReader = new ExcelReader();

        try (InputStream in = new FileInputStream(TEST_FILE_NAME)) {
            excelReader.setInputStream(in);
            Sheet sheet = excelReader.readDataSheet();
            List<ExcelReader.BaseLine> baseLineList = excelReader.readBaseLineList(sheet);


        }
    }

}
