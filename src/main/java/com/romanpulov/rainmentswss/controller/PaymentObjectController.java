package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PaymentObjectController {

    private final PaymentObjectRepository paymentObjectRepository;
    private final PaymentObjectDTOMapper paymentObjectDTOMapper;

    public PaymentObjectController(PaymentObjectRepository paymentObjectRepository, PaymentObjectDTOMapper paymentObjectDTOMapper) {
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
    }

    @GetMapping(path = "/paymentobjects", produces = MediaType.APPLICATION_JSON_VALUE)
    List<PaymentObjectDTO> all() {
        //return paymentObjectRepository.findAll();
        List<PaymentObjectDTO> result = new ArrayList<>();

        paymentObjectRepository.findAll().forEach(paymentObject -> result.add(paymentObjectDTOMapper.entityToDTO(paymentObject)));

        return result;
    }


}
