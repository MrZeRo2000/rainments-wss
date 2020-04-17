package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.ErrorResponseDTO;
import com.romanpulov.rainmentswss.exception.NotFoundException;
import com.romanpulov.rainmentswss.transform.ExcelReadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadPatchRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadPatchRequest(BadPatchRequestException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExcelReadException.class)
    public ResponseEntity<ErrorResponseDTO> handleExcelRead(ExcelReadException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
