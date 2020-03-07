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
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

        BigDecimal newProductCounter = BigDecimal.valueOf(23.4);
        newPayment.setProductCounter(newProductCounter);

        BigDecimal newPaymentAmount = BigDecimal.valueOf(12.56);
        newPayment.setPaymentAmount(newPaymentAmount);

        newPayment.setCommissionAmount(BigDecimal.valueOf(0.23));

        log.info("Saving payment entity: " + newPayment);
        paymentRepository.save(newPayment);
        log.info("Setting commission amount");
        newPayment.setCommissionAmount(BigDecimal.valueOf(0.7));
        paymentRepository.save(newPayment);

        log.info("Reading new payment");
        newPayment = paymentRepository.findAll().iterator().next();
        log.info("Checking payment group");
        assertThat(newProduct).isEqualTo(newPayment.getProduct());

        log.info("Checking payment amount");
        assertThat(newPaymentAmount).isEqualTo(newPayment.getPaymentAmount());

        log.info("Checking product counter");
        assertThat(newProductCounter).isEqualTo(newPayment.getProductCounter());

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

        //another payment
        Payment newPayment2 = new Payment();
        newPayment2.setPaymentDate(LocalDate.now());
        newPayment2.setPaymentPeriodDate(LocalDate.now().minusMonths(1L).withDayOfMonth(1));
        newPayment2.setPaymentObject(newPaymentObject);
        newPayment2.setPaymentGroup(newPaymentGroup);
        newPayment2.setProduct(product2);
        newPayment2.setPaymentAmount(new BigDecimal("63.76"));
        newPayment2.setCommissionAmount(BigDecimal.ZERO);

        paymentRepository.save(newPayment2);

        List<Payment> findByObjectDatePayments = paymentRepository.findByPaymentObjectIdAndPaymentPeriodDate(
                newPaymentObject,
                LocalDate.now().minusMonths(1L).withDayOfMonth(1),
                Sort.by("paymentGroup").ascending()
        );
        assertThat(findByObjectDatePayments.size()).isEqualTo(2);

        Payment updatingPayment = findByObjectDatePayments.get(0);
        int rows;
        Optional<Payment> updatedPayment;

        //updating product counter
        BigDecimal newProductCounterValue = BigDecimal.valueOf(9532.77);

        rows = paymentRepository.updateProductCounter(
                updatingPayment.getId(),
                newProductCounterValue,
                 LocalDate.now()
        );
        assertThat(rows).isEqualTo(1);

        updatedPayment = paymentRepository.findById(updatingPayment.getId());
        assertThat(updatedPayment.isPresent()).isTrue();
        assertThat(updatedPayment.get().getProductCounter()).isEqualTo(newProductCounterValue);

        //updating payment amount
        BigDecimal newPaymentAmountValue = BigDecimal.valueOf(634.32);

        rows = paymentRepository.updatePaymentAmount(
                updatingPayment.getId(),
                newPaymentAmountValue,
                LocalDate.now()
        );
        assertThat(rows).isEqualTo(1);

        updatedPayment = paymentRepository.findById(updatingPayment.getId());
        assertThat(updatedPayment.isPresent()).isTrue();
        assertThat(updatedPayment.get().getPaymentAmount()).isEqualTo(newPaymentAmountValue);

        //updating commission amount
        BigDecimal newCommissionAmountValue = new BigDecimal("1.23");

        rows = paymentRepository.updateCommissionAmount(
                updatingPayment.getId(),
                newCommissionAmountValue,
                LocalDate.now()
        );
        assertThat(rows).isEqualTo(1);

        updatedPayment = paymentRepository.findById(updatingPayment.getId());
        assertThat(updatedPayment.isPresent()).isTrue();
        assertThat(updatedPayment.get().getCommissionAmount()).isEqualTo(newCommissionAmountValue);

        //update payment amount to null
        Assertions.assertThrows(Exception.class, ()-> {
            paymentRepository.updatePaymentAmount(
                    updatingPayment.getId(),
                    null,
                    LocalDate.now()
            );
        });

        //delete parent entity custom handling
        Assertions.assertThrows(RuntimeException.class, ()->{
            paymentObjectRepository.deleteAll();
        });

        //delete parent entity custom handling
        Assertions.assertThrows(RuntimeException.class, ()->{
            paymentGroupRepository.deleteAll();
        });

        //delete parent entity custom handling
        Assertions.assertThrows(RuntimeException.class, ()->{
            productRepository.deleteAll();
        });

        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setName("Orphaned payment object");
        paymentObjectRepository.save(paymentObject);

        List<PaymentObject> paymentObjects = paymentObjectRepository.findAllByOrderByIdAsc();
        PaymentObject orphanedPaymentObject = paymentObjects.get(paymentObjects.size()-1);

        //deleting orphaned object should work
        paymentObjectRepository.delete(orphanedPaymentObject);
    }
}
