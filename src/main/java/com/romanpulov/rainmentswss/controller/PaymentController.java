package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentDTO;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController extends BaseRestController<Payment, PaymentDTO> {
    public PaymentController(CrudRepository<Payment, Long> repository, EntityDTOMapper<Payment, PaymentDTO> mapper) {
        super(repository, mapper, LoggerFactory.getLogger(PaymentController.class));
    }
}
