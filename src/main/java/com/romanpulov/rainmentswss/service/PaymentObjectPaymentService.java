package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentObjectPaymentService {
    private final PaymentObjectService paymentObjectService;

    private final PaymentRepository paymentRepository;


    public PaymentObjectPaymentService(PaymentObjectService paymentObjectService, PaymentRepository paymentRepository) {
        this.paymentObjectService = paymentObjectService;
        this.paymentRepository = paymentRepository;
    }

    public Map<PaymentObject, BigDecimal> getPaymentObjectTotalsByPaymentPeriod(LocalDate paymentPeriodDate) {

        List<Payment> paymentList = paymentRepository.findAllByPaymentPeriodDate(paymentPeriodDate);

        Map<PaymentObject, BigDecimal> paymentListTotals =
                paymentList
                        .stream()
                        .collect(Collectors.groupingBy(
                                Payment::getPaymentObject,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Payment::getPaymentAmount,
                                        BigDecimal::add
                                )
                                )
                        );

        Map<PaymentObject, BigDecimal> result = new LinkedHashMap<>();

        paymentObjectService.findAll().forEach(paymentObject -> {
            BigDecimal value = paymentListTotals.get(paymentObject);
            result.put(paymentObject, Optional.ofNullable(value).orElse(BigDecimal.ZERO));
        });

        return result;
    }
}
