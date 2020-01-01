package com.romanpulov.rainmentswss.entitymapper;

import com.romanpulov.rainmentswss.dto.PaymentObjectDTO;
import com.romanpulov.rainmentswss.entity.PaymentObject;

public class PaymentObjectDTOMapper implements EntityDTOMapper <PaymentObject, PaymentObjectDTO>{
    @Override
    public PaymentObjectDTO entityToDTO(PaymentObject entity) {
        return new PaymentObjectDTO(entity.getId(), entity.getName());
    }

    @Override
    public PaymentObject dtoTOEntity(PaymentObjectDTO dto) {
        return new PaymentObject(dto.getId(), dto.getName());
    }
}
