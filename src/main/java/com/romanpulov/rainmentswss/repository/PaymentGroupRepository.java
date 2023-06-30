package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentGroupRepository extends CrudRepository<PaymentGroup, Long> {
    List<PaymentGroup> findAllByOrderByOrderIdAsc();
    List<PaymentGroup> findByName(String name);
}
