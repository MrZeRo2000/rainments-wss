package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

@SpringBootTest
public class RepositoryPaymentTests {

    private static final Logger log = Logger.getLogger(RepositoryPaymentTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void mainTest() {
        Assertions.assertThrows(javax.validation.ConstraintViolationException.class, ()-> {
            Payment newPayment = new Payment();
            paymentRepository.save(newPayment);
        });
    }
}
