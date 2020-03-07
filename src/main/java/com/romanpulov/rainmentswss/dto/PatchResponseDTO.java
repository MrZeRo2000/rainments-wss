package com.romanpulov.rainmentswss.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class PatchResponseDTO {
    private int rowsAffected;

    @JsonCreator
    public PatchResponseDTO(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatchResponseDTO that = (PatchResponseDTO) o;
        return rowsAffected == that.rowsAffected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowsAffected);
    }

    @Override
    public String toString() {
        return "PatchResponseDTO{" +
                "rowsAffected=" + rowsAffected +
                '}';
    }
}
