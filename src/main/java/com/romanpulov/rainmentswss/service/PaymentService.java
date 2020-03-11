package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public int updateProductCounter(Long paymentId, BigDecimal productCounter, LocalDate paymentDate) {
        return this.paymentRepository.updateProductCounter(paymentId, productCounter, paymentDate);
    }

    @Transactional
    public int updatePaymentAmount(Long paymentId, BigDecimal paymentAmount, LocalDate paymentDate) {
        if (paymentAmount == null) {
            paymentAmount = BigDecimal.valueOf(0L);
        }
        return this.paymentRepository.updatePaymentAmount(paymentId, paymentAmount, paymentDate);
    }

    @Transactional
    public int updateCommissionAmount(Long paymentId, BigDecimal commissionAmount, LocalDate paymentDate) {
        if (commissionAmount == null) {
            commissionAmount = BigDecimal.valueOf(0L);
        }
        return this.paymentRepository.updateCommissionAmount(paymentId, commissionAmount, paymentDate);
    }

}
