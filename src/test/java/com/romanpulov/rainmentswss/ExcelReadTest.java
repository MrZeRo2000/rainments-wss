package com.romanpulov.rainmentswss;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.slf4j.Logger;

public class ExcelReadTest {
    private static final String TEST_FILE_NAME = "data/Payments.xls";

    static final Logger logger = LoggerFactory.getLogger(ExcelReadTest.class);

    @Test
    void test() {
        File xlsFile = new File(TEST_FILE_NAME);
        Assertions.assertTrue(xlsFile.exists());
    }

    @Test
    void readFile() throws Exception{
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

}
