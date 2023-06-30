package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.PaymentObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PaymentObjectRepository extends CrudRepository<PaymentObject, Long> {
    List<PaymentObject> findAllByOrderByOrderIdAsc();
}
