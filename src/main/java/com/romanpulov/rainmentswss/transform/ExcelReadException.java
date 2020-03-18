package com.romanpulov.rainmentswss.transform;

public class ExcelReadException extends Exception {
    public ExcelReadException(String message) {
        super("Error reading from Excel: " + message);
    }
}
