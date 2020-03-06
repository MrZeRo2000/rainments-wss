package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.converter.DateConverter;
import jdk.vm.ci.meta.Local;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE payment_object_id = :payment_object_id AND payment_period_date = :payment_period_date")
    List<Payment> findByPaymentObjectIdAndPaymentPeriodDate(
            @Param("payment_object_id")
            Long paymentObjectId,
            @Param("payment_period_date")
            Long paymentPeriodDate,
            Sort sort);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Payment p SET p.productCounter = :product_counter, p.paymentDate = :payment_date WHERE p.id = :payment_id")
    int updateProductCounter(
            @Param("payment_id")
            Long paymentId,
            @Param("product_counter")
            Long productCounter,
            @Param("payment_date")
            LocalDate paymentDate);
}
