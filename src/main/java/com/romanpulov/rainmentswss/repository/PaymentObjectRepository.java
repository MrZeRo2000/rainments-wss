package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.PaymentObject;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PaymentObjectRepository extends PagingAndSortingRepository<PaymentObject, Long> {
    public List<PaymentObject> findAllByOrderByIdAsc();
}
