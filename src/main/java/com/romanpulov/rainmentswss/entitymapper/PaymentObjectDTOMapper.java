package com.romanpulov.rainmentswss.entitymapper;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import org.springframework.stereotype.Component;

@Component
public class PaymentObjectDTOMapper implements EntityDTOMapper <PaymentObject, PaymentObjectDTO>{
    @Override
    public PaymentObjectDTO entityToDTO(PaymentObject entity) {
        return new PaymentObjectDTO(entity.getId(), entity.getName());
    }

    @Override
    public PaymentObject dtoTOEntity(PaymentObjectDTO dto) {
        PaymentObject entity = new PaymentObject();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public Class<?> getEntityClass() {
        return PaymentObject.class;
    }
}
