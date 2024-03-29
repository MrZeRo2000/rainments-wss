package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import com.romanpulov.rainmentswss.service.PaymentObjectService;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/payment-objects", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentObjectController extends AbstractOrderedServiceRestController<PaymentObject, PaymentObjectDTO, PaymentObjectService> {

    public PaymentObjectController(
            PaymentObjectService paymentObjectService,
            EntityDTOMapper<PaymentObject, PaymentObjectDTO> paymentObjectDTOMapper
    ) {
        super(paymentObjectService, paymentObjectDTOMapper, LoggerFactory.getLogger(PaymentObjectController.class));
    }

}
