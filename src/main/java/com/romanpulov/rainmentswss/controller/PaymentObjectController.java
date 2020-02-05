package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payment-objects", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentObjectController extends BaseRestController<PaymentObject, PaymentObjectDTO> {

    public PaymentObjectController(
            CrudRepository<PaymentObject, Long> paymentObjectRepository,
            EntityDTOMapper<PaymentObject, PaymentObjectDTO> paymentObjectDTOMapper
    ) {
        super(paymentObjectRepository, paymentObjectDTOMapper, LoggerFactory.getLogger(PaymentObjectController.class));
    }

}
