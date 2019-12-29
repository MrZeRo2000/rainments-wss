package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RepositoryPaymentObjectTests {

    private static final Logger log = Logger.getLogger(RepositoryPaymentObjectTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private PaymentObjectRepository paymentObjectRepository;

    @Test
    void mainTest() {
        Iterable<PaymentObject> paymentObjects;

        log.info("mainTest started");
        paymentObjects = paymentObjectRepository.findAll();
        assertThat(paymentObjects).isNotNull();

        PaymentObject newPaymentObject = new PaymentObject();
        newPaymentObject.setName("New Name");
        paymentObjectRepository.save(newPaymentObject);

        paymentObjects = paymentObjectRepository.findAllByOrderByIdAsc();
        PaymentObject savedEntity = paymentObjects.iterator().next();

        log.info("Saved entity: " + savedEntity);

        assertThat(savedEntity.getName()).isEqualTo(newPaymentObject.getName());

        paymentObjectRepository.deleteAll();
        paymentObjects = paymentObjectRepository.findAll();
        Assertions.assertFalse(paymentObjects.iterator().hasNext());

        log.info("getTest completed");
    }
}
