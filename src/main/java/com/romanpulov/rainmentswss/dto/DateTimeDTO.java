package com.romanpulov.rainmentswss.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.romanpulov.rainmentswss.dto.serializer.LocalDateTimeDeserializer;
import com.romanpulov.rainmentswss.dto.serializer.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

public class DateTimeDTO {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime localDateTime;

    @JsonCreator
    public DateTimeDTO(@JsonProperty("dateTime") LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeDTO that = (DateTimeDTO) o;
        return Objects.equals(localDateTime, that.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDateTime);
    }

    @Override
    public String toString() {
        return "DateTimeDTO{" +
                "localDateTime=" + localDateTime +
                '}';
    }
}
