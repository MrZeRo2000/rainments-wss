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
import com.romanpulov.rainmentswss.service.PaymentService;
import com.romanpulov.rainmentswss.service.PaymentTransformationService;
import com.romanpulov.rainmentswss.transform.ExcelReadException;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PaymentCustomController extends BaseRestController<Payment, PaymentDTO> {

    private final PaymentRepository paymentRepository;

    private final PaymentObjectRepository paymentObjectRepository;

    private final PaymentGroupRepository paymentGroupRepository;

    private final ProductRepository productRepository;

    private final PaymentObjectDTOMapper paymentObjectDTOMapper;

    private final PaymentGroupDTOMapper paymentGroupDTOMapper;

    private final ProductDTOMapper productDTOMapper;

    private final PaymentService paymentService;

    private final PaymentTransformationService paymentTransformationService;

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
            PaymentTransformationService paymentTransformationService
    ) {
        super(repository, mapper, LoggerFactory.getLogger(PaymentController.class));
        this.paymentRepository = repository;
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentGroupRepository = paymentGroupRepository;
        this.productRepository = productRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
        this.paymentGroupDTOMapper = paymentGroupDTOMapper;
        this.productDTOMapper = productDTOMapper;
        this.paymentService = paymentService;
        this.paymentTransformationService = paymentTransformationService;
    }

    @GetMapping("/payments:refs")
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

    @PostMapping("/payments:import_excel")
    ResponseEntity<RowsAffectedDTO> importExcelFile(
            @RequestParam("payment_object") PaymentObjectDTO paymentObjectDTO,
            @RequestParam("file") MultipartFile file
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
}
