package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/paymentobjects")
public class PaymentObjectController {

    private final PaymentObjectRepository paymentObjectRepository;
    private final PaymentObjectDTOMapper paymentObjectDTOMapper;

    public PaymentObjectController(PaymentObjectRepository paymentObjectRepository, PaymentObjectDTOMapper paymentObjectDTOMapper) {
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<PaymentObjectDTO>> all() {
        List<PaymentObjectDTO> result = new ArrayList<>();

        paymentObjectRepository.findAll().forEach(paymentObject -> result.add(paymentObjectDTOMapper.entityToDTO(paymentObject)));

        return ResponseEntity.ok(result);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentObjectDTO> save(@RequestBody PaymentObjectDTO paymentObjectDTO) {
        PaymentObject paymentObject = paymentObjectDTOMapper.dtoTOEntity(paymentObjectDTO);
        PaymentObject newPaymentObject = paymentObjectRepository.save(paymentObject);
        return ResponseEntity.ok(paymentObjectDTOMapper.entityToDTO(newPaymentObject));
    }
}
