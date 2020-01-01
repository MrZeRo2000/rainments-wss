package com.romanpulov.rainmentswss.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PaymentObjectDTO {
    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public PaymentObjectDTO(
            @JsonProperty("id") long id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentObjectDTO that = (PaymentObjectDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PaymentObjectDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
