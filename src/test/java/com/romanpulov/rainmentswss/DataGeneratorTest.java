package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataGeneratorTest {

    @Autowired
    private PaymentObjectRepository paymentObjectRepository;

    @Autowired
    private PaymentGroupRepository paymentGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    private PaymentObject createPaymentObject(String objectName) {
        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setName(objectName);
        return paymentObjectRepository.save(paymentObject);
    }

    private PaymentGroup createPaymentGroup(String groupName) {
        PaymentGroup paymentGroup = new PaymentGroup();
        paymentGroup.setName(groupName);
        return paymentGroupRepository.save(paymentGroup);
    }

    private Product createProduct(String productName, String unitName) {
        Product product = new Product();
        product.setName(productName);
        product.setUnitName(unitName);
        return productRepository.save(product);
    }

    private Payment createPayment(
            LocalDate paymentPeriodDate,
            PaymentObject paymentObject,
            PaymentGroup paymentGroup,
            Product product,
            BigDecimal productCounter,
            BigDecimal paymentAmount,
            BigDecimal commissionAmount) {
        Payment payment = new Payment();
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentPeriodDate(paymentPeriodDate);
        payment.setPaymentObject(paymentObject);
        payment.setPaymentGroup(paymentGroup);
        payment.setProduct(product);
        payment.setProductCounter(productCounter);
        payment.setPaymentAmount(paymentAmount);
        payment.setCommissionAmount(commissionAmount);

        return paymentRepository.save(payment);
    }

    @Test
    @Disabled
    void generateData() {
        PaymentObject paymentObject = createPaymentObject("Object 1");
        Assertions.assertTrue(paymentObject.getId() > 0);

        PaymentGroup paymentGroup = createPaymentGroup("Group 1");
        Assertions.assertTrue(paymentGroup.getId() > 0);

        Product product1 = createProduct("Product 1", "m\u00B3");
        Assertions.assertTrue(product1.getId() > 0);

        Product product2 = createProduct("Product 2", "kW");
        Assertions.assertTrue(product2.getId() > 0);

        LocalDate lastPeriodDate = LocalDate.now().withDayOfMonth(1).minusMonths(1);
        LocalDate prevPeriodDate = lastPeriodDate.minusMonths(1);

        Payment payment = createPayment(prevPeriodDate, paymentObject, paymentGroup, product1,
                BigDecimal.valueOf(13),
                BigDecimal.valueOf(44.33),
                BigDecimal.valueOf(0));
        payment = createPayment(prevPeriodDate, paymentObject, paymentGroup, product2,
                BigDecimal.valueOf(23),
                BigDecimal.valueOf(7.25),
                BigDecimal.valueOf(0));
        payment = createPayment(lastPeriodDate, paymentObject, paymentGroup, product1,
                BigDecimal.valueOf(65),
                BigDecimal.valueOf(5.22),
                BigDecimal.valueOf(0.2));
        payment = createPayment(lastPeriodDate, paymentObject, paymentGroup, product2,
                BigDecimal.valueOf(95),
                BigDecimal.valueOf(8.66),
                BigDecimal.valueOf(0.1));
    }

}
