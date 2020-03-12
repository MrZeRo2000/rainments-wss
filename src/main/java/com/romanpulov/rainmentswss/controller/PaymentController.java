package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PatchRequestDTO;
import com.romanpulov.rainmentswss.dto.RowsAffectedDTO;
import com.romanpulov.rainmentswss.dto.PaymentDTO;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entitymapper.EntityDTOMapper;
import com.romanpulov.rainmentswss.service.PaymentService;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController extends BaseRestController<Payment, PaymentDTO> {

    private final PaymentService paymentService;

    public PaymentController(
            CrudRepository<Payment, Long> repository,
            PaymentService paymentService,
            EntityDTOMapper<Payment, PaymentDTO> mapper
            ) {
        super(repository, mapper, LoggerFactory.getLogger(PaymentController.class));
        this.paymentService = paymentService;
    }

    @PatchMapping("/{id}")
    ResponseEntity<RowsAffectedDTO> partialUpdate (
            @PathVariable Long id,
            @RequestBody PatchRequestDTO patchRequest
    ) throws BadPatchRequestException {
        if (!patchRequest.getOp().equals("replace")) {
            throw new BadPatchRequestException("operation", patchRequest.getOp());
        }

        BigDecimal updateValue = null;
        if (patchRequest.getValue() != null) {
            try {
                updateValue = new BigDecimal(patchRequest.getValue());
            } catch (RuntimeException e) {
                throw new BadPatchRequestException("value", patchRequest.getValue());
            }
        }

        int result;
        switch (patchRequest.getPath()) {
            case "/productCounter":
                result = paymentService.updateProductCounter(id, updateValue, LocalDate.now());
                break;
            case "/paymentAmount":
                result = paymentService.updatePaymentAmount(id, updateValue, LocalDate.now());
                break;
            case "/commissionAmount":
                result = paymentService.updateCommissionAmount(id, updateValue, LocalDate.now());
                break;
            default:
                throw new BadPatchRequestException("path", patchRequest.getPath());
        }

        return ResponseEntity.ok(new RowsAffectedDTO(result));
    }
}
