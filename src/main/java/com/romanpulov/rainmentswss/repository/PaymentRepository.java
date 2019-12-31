package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {
}
