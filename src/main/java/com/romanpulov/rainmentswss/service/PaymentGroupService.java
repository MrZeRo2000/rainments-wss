package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentGroupService extends BaseEntityService<PaymentGroup, PaymentGroupRepository> {
    public PaymentGroupService(PaymentGroupRepository repository) {
        super(repository);
    }

    @Override
    public Iterable<PaymentGroup> findAll() {
        return repository.findAllByOrderByOrderIdAsc();
    }

    public List<PaymentGroup> findByName(String name) {
        return repository.findByName(name);
    }

    public List<PaymentGroup> findAllByOrderByOrderIdAsc() {
        return repository.findAllByOrderByOrderIdAsc();
    }

    public List<PaymentGroup> findAllByOrderByOrderIdAscIdAsc() {
        return repository.findAllByOrderByOrderIdAscIdAsc();
    }

}
