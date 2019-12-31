package com.romanpulov.rainmentswss.entity;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountConverter implements AttributeConverter<BigDecimal, Long> {
    private static BigDecimal SCALE =  BigDecimal.valueOf(100L);

    @Override
    public Long convertToDatabaseColumn(BigDecimal attribute) {
        return attribute == null? null : attribute.multiply(SCALE).longValue();
    }

    @Override
    public BigDecimal convertToEntityAttribute(Long dbData) {
        return dbData == null? null : BigDecimal.valueOf(dbData).divide(SCALE, RoundingMode.FLOOR);
    }
}
