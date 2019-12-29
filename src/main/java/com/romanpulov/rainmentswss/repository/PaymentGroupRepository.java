package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PaymentGroupRepository extends PagingAndSortingRepository<PaymentGroup, Long> {
    public List<PaymentGroup> findAllByOrderByIdAsc();
}