package com.romanpulov.rainmentswss.vo;

public enum PeriodType {
    D,
    M,
    Q;
    public static PeriodType defaultValue() {
        return PeriodType.M;
    }
}
