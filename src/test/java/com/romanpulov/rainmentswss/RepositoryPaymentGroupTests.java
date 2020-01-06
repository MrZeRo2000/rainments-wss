package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryPaymentGroupTests {

    private static final Logger log = Logger.getLogger(RepositoryPaymentGroupTests.class.getName());

    @Autowired
    private PaymentGroupRepository paymentGroupRepository;

    @Test
    void mainTest() {
        Iterable<PaymentGroup> paymentGroups;

        paymentGroups = paymentGroupRepository.findAllByOrderByIdAsc();
        assertThat(paymentGroups).isNotNull();
    }
}
