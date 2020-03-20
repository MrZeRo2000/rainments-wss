package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    public int duplicatePreviousPeriod(PaymentObject paymentObject, LocalDate paymentPeriodDate) {
        paymentPeriodDate = paymentPeriodDate.withDayOfMonth(1);

        List<Payment> currentPeriodPayments =
                this.paymentRepository.findByPaymentObjectIdAndPaymentPeriodDate(paymentObject, paymentPeriodDate, Sort.unsorted());
        if (!currentPeriodPayments.isEmpty()) {
            throw new RuntimeException("Current period is not empty");
        }

        LocalDate prevPeriodDate = paymentPeriodDate.minusMonths(1);
        List<Payment> prevPeriodPayments =
                this.paymentRepository.findByPaymentObjectIdAndPaymentPeriodDate(paymentObject, prevPeriodDate, Sort.unsorted());

        for (Payment prevPeriodPayment: prevPeriodPayments) {
            Payment newPayment = new Payment();
            newPayment.setPaymentDate(LocalDate.now());
            newPayment.setPaymentPeriodDate(paymentPeriodDate);
            newPayment.setPaymentObject(prevPeriodPayment.getPaymentObject());
            newPayment.setPaymentGroup(prevPeriodPayment.getPaymentGroup());
            newPayment.setProduct(prevPeriodPayment.getProduct());
            newPayment.setProductCounter(prevPeriodPayment.getProductCounter());
            newPayment.setPaymentAmount(BigDecimal.valueOf(0));
            newPayment.setCommissionAmount(BigDecimal.valueOf(0));

            this.paymentRepository.save(newPayment);
        }

        return prevPeriodPayments.size();
    }

}
