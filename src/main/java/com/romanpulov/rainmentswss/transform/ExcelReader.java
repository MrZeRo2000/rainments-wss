package com.romanpulov.rainmentswss.transform;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExcelReader {
    private InputStream inputStream;

    public void setInputStream(InputStream value) {
        this.inputStream = value;
    }

    DataFormatter formatter = new DataFormatter();

    public Sheet readDataSheet() throws ExcelReadException {
        try (Workbook wb = WorkbookFactory.create(inputStream)) {
            try {
                return wb.getSheetAt(0);
            } catch (IllegalArgumentException e) {
                throw new ExcelReadException("Erro reading sheet:" + e.getMessage());
            }
        } catch (IOException e) {
            throw new ExcelReadException(e.getMessage());
        }
    }

    public static class BaseLine {
        public final String productName;
        public final String groupName;

        public BaseLine(String productName, String groupName) {
            this.productName = productName;
            this.groupName = groupName;
        }

        @Override
        public String toString() {
            return "BaseLine{" +
                    "productName='" + productName + '\'' +
                    ", groupName='" + groupName + '\'' +
                    '}';
        }
    }

    private List<BaseLine> baseLineList;

    public List<BaseLine> getBaseLineList() {
        return baseLineList;
    }

    public void readBaseLineList(Sheet sheet) {
        baseLineList = new ArrayList<>();

        boolean isFirstRow = true;
        Cell prevGroupNameCell = null;
        for (Row row: sheet) {
            if (isFirstRow) {
                isFirstRow = false;
            } else {
                Cell productCell = row.getCell(0);
                if (productCell != null) {
                    String productName = formatter.formatCellValue(productCell);
                    Cell groupNameCell;
                    Cell nextGroupNameCell = row.getCell(1);
                    if ((nextGroupNameCell != null) && (!nextGroupNameCell.getStringCellValue().isEmpty())) {
                        groupNameCell = nextGroupNameCell;
                        prevGroupNameCell = nextGroupNameCell;
                    } else {
                        groupNameCell = prevGroupNameCell;
                    }
                    if (groupNameCell != null) {
                        Cell paymentTypeCell = row.getCell(2);
                        String groupName = formatter.formatCellValue(groupNameCell);
                        if ((paymentTypeCell != null) && (!paymentTypeCell.getStringCellValue().isEmpty())) {
                            groupName = groupName + "(" + formatter.formatCellValue(paymentTypeCell) +")";
                        }
                        baseLineList.add(new BaseLine(productName, groupName));
                    }
                }
            }
        }
    }
}
