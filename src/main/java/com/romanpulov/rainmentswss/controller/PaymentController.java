package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.*;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entity.converter.DateConverter;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.PaymentGroupDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.ProductDTOMapper;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.format.annotation.DateTimeFormat;
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

    private PaymentRepository paymentRepository;

    private PaymentObjectRepository paymentObjectRepository;

    private PaymentGroupRepository paymentGroupRepository;

    private ProductRepository productRepository;

    private PaymentObjectDTOMapper paymentObjectDTOMapper;

    private PaymentGroupDTOMapper paymentGroupDTOMapper;

    private ProductDTOMapper productDTOMapper;

    private DateConverter dateConverter;

    public PaymentController(
            PaymentRepository repository,
            EntityDTOMapper<Payment, PaymentDTO> mapper,
            PaymentObjectRepository paymentObjectRepository,
            PaymentGroupRepository paymentGroupRepository,
            ProductRepository productRepository,
            PaymentObjectDTOMapper paymentObjectDTOMapper,
            PaymentGroupDTOMapper paymentGroupDTOMapper,
            ProductDTOMapper productDTOMapper,
            DateConverter dateConverter
            ) {
        super(repository, mapper, LoggerFactory.getLogger(PaymentController.class));
        this.paymentRepository = repository;
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentGroupRepository = paymentGroupRepository;
        this.productRepository = productRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
        this.paymentGroupDTOMapper = paymentGroupDTOMapper;
        this.productDTOMapper = productDTOMapper;
        this.dateConverter = dateConverter;
    }

    @GetMapping("/refs")
    ResponseEntity<PaymentRefsDTO> getRefsByPaymentObjectIdAndDate(
            @RequestParam("paymentObjectId")
            Long paymentObjectId,
            @RequestParam("paymentPeriodDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDate paymentPeriodDate
    ) {

        List<PaymentObjectDTO> paymentObjectList = paymentObjectRepository
                .findAllByOrderByIdAsc()
                .stream()
                .map(paymentObjectDTOMapper::entityToDTO)
                .collect(Collectors.toList());


        List<PaymentGroupDTO> paymentGroupList = paymentGroupRepository
                .findAllByOrderByIdAsc()
                .stream()
                .map(paymentGroupDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        List<ProductDTO> productList = productRepository
                .findAllByOrderByIdAsc()
                .stream()
                .map(productDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        List<PaymentDTO> paymentList = paymentRepository
                .findByPaymentObjectIdAndPaymentPeriodDate(
                paymentObjectId,
                dateConverter.convertToDatabaseColumn(paymentPeriodDate),
                Sort.by("paymentGroup", "product"))
                .stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());

        List<PaymentDTO> prevPeriodPaymentList = paymentRepository
                .findByPaymentObjectIdAndPaymentPeriodDate(
                        paymentObjectId,
                        dateConverter.convertToDatabaseColumn(paymentPeriodDate.minusMonths(1L)),
                        Sort.by("paymentGroup", "product"))
                .stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());

        PaymentRefsDTO result = new PaymentRefsDTO(
                paymentList,
                prevPeriodPaymentList,
                paymentObjectList,
                paymentGroupList,
                productList
        );


        return ResponseEntity.ok(result);
    }
}
