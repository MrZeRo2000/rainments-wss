package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentGroupDTO;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment-groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentGroupController extends BaseRestController<PaymentGroup, PaymentGroupDTO> {

    public PaymentGroupController(
            CrudRepository<PaymentGroup, Long> repository,
            EntityDTOMapper<PaymentGroup, PaymentGroupDTO> mapper)
    {
        super(repository, mapper, LoggerFactory.getLogger(PaymentGroupController.class));
    }
}
