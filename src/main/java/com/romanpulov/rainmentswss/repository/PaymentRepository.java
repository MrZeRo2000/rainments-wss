package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entity.converter.AmountConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import javax.persistence.Convert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.paymentObject = :payment_object AND p.paymentPeriodDate = :payment_period_date")
    List<Payment> findByPaymentObjectIdAndPaymentPeriodDate(
            @Param("payment_object")
            PaymentObject paymentObject,
            @Param("payment_period_date")
            LocalDate paymentPeriodDate,
            Sort sort);

    @Query("SELECT SUM(p.paymentAmount) FROM Payment p WHERE p.paymentObject = :payment_object AND p.paymentPeriodDate = :payment_period_date")
    BigDecimal sumByPaymentObjectIdAndPaymentPeriodDate(
            @Param("payment_object")
            PaymentObject paymentObject,
            @Param("payment_period_date")
            LocalDate paymentPeriodDate
    );

    List<Payment> findAllByPaymentPeriodDate(@Param("payment_period_date") LocalDate paymentPeriodDate);

    List<Payment> findAllByPaymentPeriodDateAndPaymentObject(
            @Param("payment_period_date") LocalDate paymentPeriodDate,
            @Param("payment_object") PaymentObject paymentObject
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.productCounter = :product_counter, p.paymentDate = :payment_date WHERE p.id = :payment_id")
    int updateProductCounter(
            @Param("payment_id")
            Long paymentId,
            @Param("product_counter")
            BigDecimal productCounter,
            @Param("payment_date")
            LocalDate paymentDate);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.paymentAmount = :payment_amount, p.paymentDate = :payment_date WHERE p.id = :payment_id")
    int updatePaymentAmount(
            @Param("payment_id")
            Long paymentId,
            @Param("payment_amount")
            BigDecimal paymentAmount,
            @Param("payment_date")
            LocalDate paymentDate);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.commissionAmount = :commission_amount, p.paymentDate = :payment_date WHERE p.id = :payment_id")
    int updateCommissionAmount(
            @Param("payment_id")
                    Long paymentId,
            @Param("commission_amount")
                    BigDecimal commissionAmount,
            @Param("payment_date")
                    LocalDate paymentDate);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.paymentGroup = :paymentGroupTo WHERE p.paymentObject = :paymentObject AND p.paymentGroup = :paymentGroupFrom")
    int updatePaymentGroup(
            @NonNull
            @Param("paymentObject")
            PaymentObject paymentObject,
            @NonNull
            @Param("paymentGroupFrom")
            PaymentGroup paymentGroupFrom,
            @NonNull
            @Param("paymentGroupTo")
            PaymentGroup paymentGroupTo
    );
}
