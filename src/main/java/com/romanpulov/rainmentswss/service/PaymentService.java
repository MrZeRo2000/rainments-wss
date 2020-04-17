package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService extends AbstractEntityService<Payment, PaymentRepository> {

    public PaymentService(PaymentRepository repository) {
        super(repository);
    }

    @Transactional
    public int updateProductCounter(Long paymentId, BigDecimal productCounter, LocalDate paymentDate) {
        return this.repository.updateProductCounter(paymentId, productCounter, paymentDate);
    }

    @Transactional
    public int updatePaymentAmount(Long paymentId, BigDecimal paymentAmount, LocalDate paymentDate) {
        if (paymentAmount == null) {
            paymentAmount = BigDecimal.valueOf(0L);
        }
        return this.repository.updatePaymentAmount(paymentId, paymentAmount, paymentDate);
    }

    @Transactional
    public int updateCommissionAmount(Long paymentId, BigDecimal commissionAmount, LocalDate paymentDate) {
        if (commissionAmount == null) {
            commissionAmount = BigDecimal.valueOf(0L);
        }
        return this.repository.updateCommissionAmount(paymentId, commissionAmount, paymentDate);
    }

    @Transactional
    public int updatePaymentGroup(PaymentObject paymentObject, PaymentGroup paymentGroupFrom, PaymentGroup paymentGroupTo) {
        return this.repository.updatePaymentGroup(paymentObject, paymentGroupFrom, paymentGroupTo);
    }

    @Transactional
    public int duplicatePreviousPeriod(PaymentObject paymentObject, LocalDate paymentPeriodDate) {
        paymentPeriodDate = paymentPeriodDate.withDayOfMonth(1);

        List<Payment> currentPeriodPayments =
                this.repository.findByPaymentObjectIdAndPaymentPeriodDate(paymentObject, paymentPeriodDate, Sort.unsorted());
        if (!currentPeriodPayments.isEmpty()) {
            throw new RuntimeException("Current period is not empty");
        }

        LocalDate prevPeriodDate = paymentPeriodDate.minusMonths(1);
        List<Payment> prevPeriodPayments =
                this.repository.findByPaymentObjectIdAndPaymentPeriodDate(paymentObject, prevPeriodDate, Sort.unsorted());

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

            this.repository.save(newPayment);
        }

        return prevPeriodPayments.size();
    }

}
