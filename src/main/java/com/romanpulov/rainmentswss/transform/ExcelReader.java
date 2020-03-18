package com.romanpulov.rainmentswss.transform;

import com.romanpulov.rainmentswss.dto.ExtPaymentDTO;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static class DateColumnMapping {
        public final LocalDate periodDate;
        public final int paymentColumn;
        public final int commissionColumn;

        public DateColumnMapping(LocalDate periodDate, int paymentColumn, int commissionColumn) {
            this.periodDate = periodDate;
            this.paymentColumn = paymentColumn;
            this.commissionColumn = commissionColumn;
        }

        @Override
        public String toString() {
            return "DateColumn{" +
                    "periodDate=" + periodDate +
                    ", paymentColumn=" + paymentColumn +
                    ", commissionColumn=" + commissionColumn +
                    '}';
        }
    }

    public List<BaseLine> readBaseLineList(Sheet sheet) {
        List<BaseLine> baseLineList = new ArrayList<>();

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
        return baseLineList;
    }

    public List<DateColumnMapping> readDateColumnMapping(Sheet sheet) {
        List<DateColumnMapping> columnMappings = new ArrayList<>();

        Row columnRow = sheet.getRow(0);

        int paymentColumn = 0;
        LocalDate periodDate = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (int i = 0; i < columnRow.getLastCellNum(); i++) {
            Cell cell = columnRow.getCell(i);
            if ((cell != null) && (!formatter.formatCellValue(cell).isEmpty())) {
                if ((cell.getCellType() == CellType.NUMERIC) && (DateUtil.isCellDateFormatted(cell))) {
                    periodDate = cell.getLocalDateTimeCellValue().toLocalDate();
                    paymentColumn = i;
                } else if ((periodDate != null)
                        && (i - paymentColumn == 1)
                        && (formatter.formatCellValue(cell).contains(periodDate.format(dateTimeFormatter)))) {
                    columnMappings.add(new DateColumnMapping(periodDate, paymentColumn, i));
                    periodDate = null;
                    paymentColumn = 0;
                }
            }
        }

        return columnMappings;
    }

    public List<ExtPaymentDTO> readContent(Sheet sheet) {
        List<ExtPaymentDTO> content = new ArrayList<>();

        List<BaseLine> baseLineList = readBaseLineList(sheet);

        return content;
    }
}
