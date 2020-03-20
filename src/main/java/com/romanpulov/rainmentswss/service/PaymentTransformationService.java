package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.dto.ExtPaymentDTO;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import com.romanpulov.rainmentswss.transform.ExcelReader;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentTransformationService {
    private final PaymentGroupRepository paymentGroupRepository;

    private final ProductRepository productRepository;

    private final PaymentRepository paymentRepository;

    public final ObjectProvider<ExcelReader> excelReaderObjectProvider;

    public PaymentTransformationService(
            PaymentGroupRepository paymentGroupRepository,
            ProductRepository productRepository,
            PaymentRepository paymentRepository,
            ObjectProvider<ExcelReader> excelReaderObjectProvider
    ) {
        this.paymentGroupRepository = paymentGroupRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
        this.excelReaderObjectProvider = excelReaderObjectProvider;
    }

    public Map<String, PaymentGroup> transformPaymentGroups(List<ExtPaymentDTO> data) {
        Map<String, PaymentGroup> result = new HashMap<>();

        return result;
    }

    public Map<String, Product> transformProducts(List<ExtPaymentDTO> data) {
        Map<String, Product> result = new HashMap<>();

        return result;
    }

    public int transformExtData(List<ExtPaymentDTO> data) {
        int rowsAffected = 0;

        return rowsAffected;
    }
}
