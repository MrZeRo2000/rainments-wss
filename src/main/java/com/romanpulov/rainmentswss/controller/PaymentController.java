package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentDTO;
import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.dto.PaymentRefsDTO;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.PaymentGroupDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.ProductDTOMapper;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController extends BaseRestController<Payment, PaymentDTO> {

    private PaymentObjectRepository paymentObjectRepository;

    private PaymentGroupRepository paymentGroupRepository;

    private ProductRepository productRepository;

    private PaymentObjectDTOMapper paymentObjectDTOMapper;

    private PaymentGroupDTOMapper paymentGroupDTOMapper;

    private ProductDTOMapper productDTOMapper;

    public PaymentController(
            CrudRepository<Payment, Long> repository,
            EntityDTOMapper<Payment,
            PaymentDTO> mapper,
            PaymentObjectRepository paymentObjectRepository,
            PaymentGroupRepository paymentGroupRepository,
            ProductRepository productRepository,
            PaymentObjectDTOMapper paymentObjectDTOMapper,
            PaymentGroupDTOMapper paymentGroupDTOMapper,
            ProductDTOMapper productDTOMapper
            ) {
        super(repository, mapper, LoggerFactory.getLogger(PaymentController.class));
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentGroupRepository = paymentGroupRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/refs")
    ResponseEntity<PaymentRefsDTO> getRefsByPaymentObjectIdAndDate(
            @RequestParam("paymentObjectId") Long paymentObjectId,
            @RequestParam("paymentPeriodDate") LocalDate paymentPeriodDate) {

        List<PaymentObjectDTO> paymentObjectList = paymentObjectRepository.findAllByOrderByIdAsc().stream().map(paymentObject -> paymentObjectDTOMapper.entityToDTO(paymentObject)).collect(Collectors.toList());


        //PaymentRefsDTO result = new PaymentRefsDTO()

        return ResponseEntity.ok(null);
    }
}
