package com.romanpulov.rainmentswss.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ExtPaymentDTO {
    private final String productName;

    private final String groupName;

    private final LocalDate PaymentPeriodDate;

    private final BigDecimal paymentAmount;

    private final BigDecimal commissionAmount;

    public ExtPaymentDTO(String productName, String groupName, LocalDate paymentPeriodDate, BigDecimal paymentAmount, BigDecimal commissionAmount) {
        this.productName = productName;
        this.groupName = groupName;
        PaymentPeriodDate = paymentPeriodDate;
        this.paymentAmount = paymentAmount;
        this.commissionAmount = commissionAmount;
    }

    public String getProductName() {
        return productName;
    }

    public String getGroupName() {
        return groupName;
    }

    public LocalDate getPaymentPeriodDate() {
        return PaymentPeriodDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtPaymentDTO that = (ExtPaymentDTO) o;
        return productName.equals(that.productName) &&
                groupName.equals(that.groupName) &&
                PaymentPeriodDate.equals(that.PaymentPeriodDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, groupName, PaymentPeriodDate);
    }

    @Override
    public String toString() {
        return "ExtPaymentDTO{" +
                "productName='" + productName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", PaymentPeriodDate=" + PaymentPeriodDate +
                ", paymentAmount=" + paymentAmount +
                ", commissionAmount=" + commissionAmount +
                '}';
    }
}
