package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.*;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentObject;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
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

    public PaymentController(
            PaymentRepository repository,
            EntityDTOMapper<Payment, PaymentDTO> mapper,
            PaymentObjectRepository paymentObjectRepository,
            PaymentGroupRepository paymentGroupRepository,
            ProductRepository productRepository,
            PaymentObjectDTOMapper paymentObjectDTOMapper,
            PaymentGroupDTOMapper paymentGroupDTOMapper,
            ProductDTOMapper productDTOMapper
            ) {
        super(repository, mapper, LoggerFactory.getLogger(PaymentController.class));
        this.paymentRepository = repository;
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentGroupRepository = paymentGroupRepository;
        this.productRepository = productRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
        this.paymentGroupDTOMapper = paymentGroupDTOMapper;
        this.productDTOMapper = productDTOMapper;
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

        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setId(paymentObjectId);

        List<PaymentDTO> paymentList = paymentRepository
                .findByPaymentObjectIdAndPaymentPeriodDate(
                    paymentObject,
                    paymentPeriodDate,
                    Sort.by("paymentGroup", "product"))
                .stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());

        List<PaymentDTO> prevPeriodPaymentList = paymentRepository
                .findByPaymentObjectIdAndPaymentPeriodDate(
                        paymentObject,
                        paymentPeriodDate.minusMonths(1L),
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

    @PatchMapping("/{id}")
    ResponseEntity<PatchResponseDTO> partialUpdate (
            @PathVariable Long id,
            @RequestBody PatchRequestDTO patchRequest
    ) throws BadPatchRequestException {
        if (!patchRequest.getOp().equals("replace")) {
            throw new BadPatchRequestException("operation", patchRequest.getOp());
        }

        BigDecimal updateValue;
        try {
            updateValue = new BigDecimal(patchRequest.getValue());
        } catch (NumberFormatException e) {
            throw new BadPatchRequestException("value", patchRequest.getValue());
        }

        int result;
        switch (patchRequest.getPath()) {
            case "/productCounter":
                // return ResponseEntity.ok(paymentRepository.)
                result = paymentRepository.updateProductCounter(id, updateValue, LocalDate.now());
                break;
            case "/paymentAmount":
                result = paymentRepository.updatePaymentAmount(id, updateValue, LocalDate.now());
                break;
            case "/commissionAmount":
                result = paymentRepository.updateCommissionAmount(id, updateValue, LocalDate.now());
                break;
            default:
                throw new BadPatchRequestException("path", patchRequest.getPath());
        }

        return ResponseEntity.ok(new PatchResponseDTO(result));
    }
}
