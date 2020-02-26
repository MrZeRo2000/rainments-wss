package com.romanpulov.rainmentswss.dto;

import java.util.List;

/**
 * Payment with references to be used by front end for editing
 */
public class PaymentRefsDTO {
    private final List<PaymentDTO> paymentList;

    private final List<PaymentDTO> prevPeriodPaymentList;

    private final List<PaymentObjectDTO> paymentObjectList;

    private final List<PaymentGroupDTO> paymentGroupList;

    private final List<ProductDTO> productList;

    public PaymentRefsDTO(
            List<PaymentDTO> paymentList,
            List<PaymentDTO> prevPeriodPaymentList,
            List<PaymentObjectDTO> paymentObjectList,
            List<PaymentGroupDTO> paymentGroupList,
            List<ProductDTO> productList
    ) {
        this.paymentList = paymentList;
        this.prevPeriodPaymentList = prevPeriodPaymentList;
        this.paymentObjectList = paymentObjectList;
        this.paymentGroupList = paymentGroupList;
        this.productList = productList;
    }

    public List<PaymentDTO> getPaymentList() {
        return paymentList;
    }

    public List<PaymentDTO> getPrevPeriodPaymentList() {
        return prevPeriodPaymentList;
    }

    public List<PaymentObjectDTO> getPaymentObjectList() {
        return paymentObjectList;
    }

    public List<PaymentGroupDTO> getPaymentGroupList() {
        return paymentGroupList;
    }

    public List<ProductDTO> getProductList() {
        return productList;
    }
}
