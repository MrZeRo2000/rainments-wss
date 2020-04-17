package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentObjectService extends BaseEntityService<PaymentObject, PaymentObjectRepository>{
    public PaymentObjectService(PaymentObjectRepository repository) {
        super(repository);
    }
}
