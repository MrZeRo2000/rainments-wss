package com.romanpulov.rainmentswss.controller;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/payment-objects", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentObjectController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentObjectRepository paymentObjectRepository;
    private final PaymentObjectDTOMapper paymentObjectDTOMapper;

    public PaymentObjectController(PaymentObjectRepository paymentObjectRepository, PaymentObjectDTOMapper paymentObjectDTOMapper) {
        this.paymentObjectRepository = paymentObjectRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
    }

    @GetMapping("")
    ResponseEntity<List<PaymentObjectDTO>> all() {
        List<PaymentObjectDTO> result = new ArrayList<>();

        paymentObjectRepository.findAll().forEach(paymentObject -> result.add(paymentObjectDTOMapper.entityToDTO(paymentObject)));

        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    ResponseEntity<PaymentObjectDTO> post(@RequestBody PaymentObjectDTO paymentObjectDTO) {
        PaymentObject paymentObject = paymentObjectDTOMapper.dtoTOEntity(paymentObjectDTO);
        PaymentObject newPaymentObject = paymentObjectRepository.save(paymentObject);
        return ResponseEntity.ok(paymentObjectDTOMapper.entityToDTO(newPaymentObject));
    }

    @PutMapping("/{id}")
    ResponseEntity<PaymentObjectDTO> put(@PathVariable Long id, @RequestBody PaymentObjectDTO paymentObjectDTO) {
        Optional<PaymentObject> paymentObject = paymentObjectRepository.findById(id);
        if (paymentObject.isPresent()) {
            PaymentObject updatedPaymentObject = paymentObjectDTOMapper.dtoTOEntity(paymentObjectDTO);
            updatedPaymentObject.setId(id);
            PaymentObject newPaymentObject = paymentObjectRepository.save(updatedPaymentObject);
            return ResponseEntity.ok(paymentObjectDTOMapper.entityToDTO(newPaymentObject));
        } else {
            logger.error("Entity with id=" + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (paymentObjectRepository.findById(id).isPresent()) {
            paymentObjectRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Entity with id=" + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
    }
}
