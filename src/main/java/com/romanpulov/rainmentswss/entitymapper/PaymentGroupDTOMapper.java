package com.romanpulov.rainmentswss.entitymapper;

import com.romanpulov.rainmentswss.dto.PaymentGroupDTO;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import org.springframework.stereotype.Component;

@Component
public class PaymentGroupDTOMapper implements EntityDTOMapper<PaymentGroup, PaymentGroupDTO> {
    @Override
    public PaymentGroupDTO entityToDTO(PaymentGroup entity) {
        return new PaymentGroupDTO(entity.getId(), entity.getName(), entity.getUrl());
    }

    @Override
    public PaymentGroup dtoTOEntity(PaymentGroupDTO dto) {
        PaymentGroup entity = new PaymentGroup();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setUrl(dto.getUrl());
        return entity;
    }

    @Override
    public Class<?> getEntityClass() {
        return PaymentGroup.class;
    }
}
