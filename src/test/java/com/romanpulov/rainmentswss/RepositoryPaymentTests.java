package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RepositoryPaymentTests {

    private static final Logger log = Logger.getLogger(RepositoryPaymentTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentObjectRepository paymentObjectRepository;

    @Autowired
    private PaymentGroupRepository paymentGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void mainTest() {
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, ()-> {
            Payment newPayment = new Payment();
            paymentRepository.save(newPayment);
        });

        Payment newPayment = new Payment();
        newPayment.setPaymentDate(LocalDate.now());
        newPayment.setPaymentPeriodDate(LocalDate.now().minusMonths(1L).withDayOfMonth(1));

        //payment object
        PaymentObject newPaymentObject = new PaymentObject();
        newPaymentObject.setName("New Payment Object");
        paymentObjectRepository.save(newPaymentObject);
        newPaymentObject = paymentObjectRepository.findAll().iterator().next();
        newPayment.setPaymentObject(newPaymentObject);

        //payment group
        PaymentGroup newPaymentGroup = new PaymentGroup();
        newPaymentGroup.setName("New Payment Group");
        paymentGroupRepository.save(newPaymentGroup);
        newPaymentGroup = paymentGroupRepository.findAll().iterator().next();
        newPayment.setPaymentGroup(newPaymentGroup);

        //product
        Product newProduct = new Product();
        newProduct.setName("New Product Name");
        productRepository.save(newProduct);
        newProduct = productRepository.findAll().iterator().next();
        newPayment.setProduct(newProduct);

        newPayment.setProductCounter("Product Counter");

        BigDecimal newPaymentAmount = new BigDecimal("12.56");
        newPayment.setPaymentAmount(newPaymentAmount);

        newPayment.setCommissionAmount(new BigDecimal("0.23"));

        log.info("Saving payment entity: " + newPayment);
        paymentRepository.save(newPayment);
        log.info("Setting commission amount");
        newPayment.setCommissionAmount(new BigDecimal("0.7"));

        log.info("Reading new payment");
        newPayment = paymentRepository.findAll().iterator().next();
        log.info("Checking payment group");
        assertThat(newProduct).isEqualTo(newPayment.getProduct());

        log.info("Checking payment amount");
        assertThat(newPaymentAmount).isEqualTo(newPayment.getPaymentAmount());

        //update to another product
        String product2Name = "Product 2";
        Product product2 = new Product();
        product2.setName(product2Name);
        productRepository.save(product2);
        product2 = productRepository.findAllByOrderByIdAsc().get(1);
        assertThat(product2.getName()).isEqualTo(product2Name);
        newPayment.setProduct(product2);

        paymentRepository.save(newPayment);
        newPayment = paymentRepository.findAll().iterator().next();
        assertThat(product2).isEqualTo(newPayment.getProduct());
        assertThat(newPaymentAmount).isEqualTo(newPayment.getPaymentAmount());
    }
}
