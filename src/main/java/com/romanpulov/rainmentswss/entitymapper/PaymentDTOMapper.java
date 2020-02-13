package com.romanpulov.rainmentswss.entitymapper;

import com.romanpulov.rainmentswss.dto.PaymentDTO;
import com.romanpulov.rainmentswss.entity.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentDTOMapper implements EntityDTOMapper<Payment, PaymentDTO> {

    private PaymentObjectDTOMapper paymentObjectDTOMapper;
    private PaymentGroupDTOMapper paymentGroupDTOMapper;
    private ProductDTOMapper productDTOMapper;

    public PaymentDTOMapper(
            PaymentObjectDTOMapper paymentObjectDTOMapper,
            PaymentGroupDTOMapper paymentGroupDTOMapper,
            ProductDTOMapper productDTOMapper
    ) {
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
        this.paymentGroupDTOMapper = paymentGroupDTOMapper;
        this.productDTOMapper = productDTOMapper;
    }

    @Override
    public PaymentDTO entityToDTO(Payment entity) {
        return new PaymentDTO(
            entity.getId(),
            entity.getPaymentDate(),
            entity.getPaymentPeriodDate(),
            paymentObjectDTOMapper.entityToDTO(entity.getPaymentObject()),
            paymentGroupDTOMapper.entityToDTO(entity.getPaymentGroup()),
            productDTOMapper.entityToDTO(entity.getProduct()),
            entity.getProductCounter(),
            entity.getPaymentAmount(),
            entity.getCommissionAmount()
        );
    }

    @Override
    public Payment dtoTOEntity(PaymentDTO dto) {
        Payment entity = new Payment();

        entity.setId(dto.getId());

        entity.setPaymentDate(dto.getDate());
        entity.setPaymentPeriodDate(dto.getPeriodDate());
        entity.setPaymentObject(paymentObjectDTOMapper.dtoTOEntity(dto.getPaymentObject()));
        entity.setPaymentGroup(paymentGroupDTOMapper.dtoTOEntity(dto.getPaymentGroup()));
        entity.setProduct(productDTOMapper.dtoTOEntity(dto.getProduct()));
        entity.setProductCounter(dto.getProductCounter());
        entity.setPaymentAmount(dto.getPaymentAmount());
        entity.setCommissionAmount(dto.getCommissionAmount());

        return entity;
    }
}
