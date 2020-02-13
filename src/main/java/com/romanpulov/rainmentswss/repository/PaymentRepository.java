package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.converter.DateConverter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

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
}
