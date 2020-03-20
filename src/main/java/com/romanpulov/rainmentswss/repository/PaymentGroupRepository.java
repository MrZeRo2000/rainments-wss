package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PaymentGroupRepository extends PagingAndSortingRepository<PaymentGroup, Long> {
    List<PaymentGroup> findAllByOrderByIdAsc();
    List<PaymentGroup> findByName(String name);
}
