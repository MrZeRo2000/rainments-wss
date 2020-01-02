package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.PaymentObject;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

public interface PaymentObjectRepository extends PagingAndSortingRepository<PaymentObject, Long> {
    List<PaymentObject> findAllByOrderByIdAsc();
}
