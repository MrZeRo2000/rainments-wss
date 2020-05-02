package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.*;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.PaymentGroupDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.entitymapper.ProductDTOMapper;
import com.romanpulov.rainmentswss.exception.CommonEntityNotFoundException;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import com.romanpulov.rainmentswss.service.PaymentObjectPaymentService;
import com.romanpulov.rainmentswss.service.PaymentService;
import com.romanpulov.rainmentswss.service.PaymentTransformationService;
import com.romanpulov.rainmentswss.transform.ExcelReadException;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PaymentCustomController {

    private final PaymentRepository paymentRepository;

    private final PaymentObjectRepository paymentObjectRepository;

    private final PaymentGroupRepository paymentGroupRepository;

    private final ProductRepository productRepository;

    private final PaymentObjectDTOMapper paymentObjectDTOMapper;

    private final PaymentGroupDTOMapper paymentGroupDTOMapper;

    private final ProductDTOMapper productDTOMapper;

    private final PaymentService paymentService;

    private final PaymentObjectPaymentService paymentObjectPaymentService;

    private final PaymentTransformationService paymentTransformationService;

    protected final EntityDTOMapper<Payment, PaymentDTO> mapper;

    public PaymentCustomController(
            PaymentRepository repository,
            EntityDTOMapper<Payment, PaymentDTO> mapper,
            PaymentObjectRepository paymentObjectRepository,
            PaymentGroupRepository paymentGroupRepository,
            ProductRepository productRepository,
            PaymentObjectDTOMapper paymentObjectDTOMapper,
            PaymentGroupDTOMapper paymentGroupDTOMapper,
            ProductDTOMapper productDTOMapper,
            PaymentService paymentService,
            PaymentObjectPaymentService paymentObjectPaymentService,
            PaymentTransformationService paymentTransformationService
    ) {
        this.paymentRepository = repository;
        this.mapper = mapper;
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentGroupRepository = paymentGroupRepository;
        this.productRepository = productRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
        this.paymentGroupDTOMapper = paymentGroupDTOMapper;
        this.productDTOMapper = productDTOMapper;
        this.paymentService = paymentService;
        this.paymentObjectPaymentService = paymentObjectPaymentService;
        this.paymentTransformationService = paymentTransformationService;
    }

    @GetMapping("/payments:refs")
    ResponseEntity<PaymentRefsDTO> getRefsByPaymentObjectIdAndDate(
            @RequestParam("paymentObjectId")
                    Long paymentObjectId,
            @RequestParam("paymentPeriodDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDate paymentPeriodDate
    ) throws CommonEntityNotFoundException {

        List<PaymentObjectDTO> paymentObjectList = paymentObjectRepository
                .findAllByOrderByOrderIdAsc()
                .stream()
                .map(paymentObjectDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        PaymentObject paymentObject = paymentObjectRepository
                .findById(paymentObjectId)
                .orElseThrow(() -> new CommonEntityNotFoundException(paymentObjectId)
        );

        PaymentObjectDTO paymentObjectDTO = paymentObjectDTOMapper.entityToDTO(paymentObject);

        List<PaymentGroupDTO> paymentGroupList = paymentGroupRepository
                .findAllByOrderByOrderIdAsc()
                .stream()
                .map(paymentGroupDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        List<ProductDTO> productList = productRepository
                .findAllByOrderByOrderIdAsc()
                .stream()
                .map(productDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        List<PaymentDTO> paymentList = paymentRepository
                .findByPaymentObjectIdAndPaymentPeriodDate(
                        paymentObject,
                        paymentPeriodDate,
                        Sort.by("paymentGroup.orderId", "product.orderId"))
                .stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());

        List<PaymentDTO> prevPeriodPaymentList = paymentRepository
                .findByPaymentObjectIdAndPaymentPeriodDate(
                        paymentObject,
                        paymentPeriodDate.minusMonths(1L),
                        Sort.by("paymentGroup.orderId", "product.orderId"))
                .stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());

        PaymentRefsDTO result = new PaymentRefsDTO(
                paymentList,
                prevPeriodPaymentList,
                paymentObjectDTO,
                paymentGroupList,
                productList
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/payments:duplicate_previous_period")
    ResponseEntity<RowsAffectedDTO> duplicatePreviousPeriod(
            @RequestParam("paymentObjectId")
                    Long paymentObjectId,
            @RequestParam("paymentPeriodDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDate paymentPeriodDate
    ) {
        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setId(paymentObjectId);

        int rowsAffected = this.paymentService.duplicatePreviousPeriod(paymentObject, paymentPeriodDate);

        return ResponseEntity.ok(new RowsAffectedDTO(rowsAffected));
    }

    @PostMapping(value="/payments:import_excel")
    ResponseEntity<RowsAffectedDTO> importExcelFile(
            @RequestPart("paymentObject")
                    PaymentObjectDTO paymentObjectDTO,
            @RequestPart("file") MultipartFile file
    )  throws ExcelReadException {
        PaymentObject paymentObject = paymentObjectDTOMapper.dtoTOEntity(paymentObjectDTO);

        try {
            int rowsAffected = paymentTransformationService.readAndTransformExcelStream(
                    paymentObject,
                    file.getInputStream()
            );
            return ResponseEntity.ok(new RowsAffectedDTO(rowsAffected));
        } catch (IOException e) {
            throw new ExcelReadException("Error reading file: " + e.getMessage());
        }
    }

    @GetMapping("/payments:payment_object_group_refs")
    ResponseEntity<PaymentObjectGroupRefsDTO> getPaymentObjectGroupRefs() {

        List<PaymentObjectDTO> paymentObjectList = paymentObjectRepository
                .findAllByOrderByOrderIdAsc()
                .stream()
                .map(paymentObjectDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        List<PaymentGroupDTO> paymentGroupList = paymentGroupRepository
                .findAllByOrderByOrderIdAsc()
                .stream()
                .map(paymentGroupDTOMapper::entityToDTO)
                .collect(Collectors.toList());

        PaymentObjectGroupRefsDTO result = new PaymentObjectGroupRefsDTO(
                paymentObjectList,
                paymentGroupList
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping(value="/payments:update_payment_group")
    ResponseEntity<RowsAffectedDTO> updatePaymentGroup (
            @RequestParam("paymentObjectId")
            Long paymentObjectId,
            @RequestParam("paymentGroupFromId")
            Long paymentGroupFromId,
            @RequestParam("paymentGroupToId")
            Long paymentGroupToId
    ) {

        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setId(paymentObjectId);

        PaymentGroup paymentGroupFrom = new PaymentGroup();
        paymentGroupFrom.setId(paymentGroupFromId);

        PaymentGroup paymentGroupTo = new PaymentGroup();
        paymentGroupTo.setId(paymentGroupToId);

        int rowsAffected = paymentService.updatePaymentGroup(paymentObject, paymentGroupFrom, paymentGroupTo);
        return ResponseEntity.ok(new RowsAffectedDTO(rowsAffected));
    }

    @GetMapping("/payments:payment_object_totals_by_payment_period")
    ResponseEntity<List<PaymentObjectTotalsDTO>> getPaymentObjectTotalsByPaymentPeriod(
        @RequestParam("paymentPeriodDate")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDate paymentPeriodDate
    ) {
        List<PaymentObjectTotalsDTO> result = new ArrayList<>();

        Map<PaymentObject, BigDecimal> totals = paymentObjectPaymentService.getPaymentObjectTotalsByPaymentPeriod(paymentPeriodDate);
        totals.forEach((paymentObject, value) ->
            result.add(
                new PaymentObjectTotalsDTO(
                    paymentPeriodDate,
                    paymentObjectDTOMapper.entityToDTO(paymentObject),
                    value
                )
            )
        );

        return ResponseEntity.ok(result);
    }
}
