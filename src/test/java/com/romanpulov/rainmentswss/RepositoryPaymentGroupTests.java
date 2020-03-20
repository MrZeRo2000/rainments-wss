package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryPaymentGroupTests {

    private static final Logger log = Logger.getLogger(RepositoryPaymentGroupTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private PaymentGroupRepository paymentGroupRepository;

    @Test
    void mainTest() {
        Iterable<PaymentGroup> paymentGroups;

        paymentGroups = paymentGroupRepository.findAllByOrderByIdAsc();
        assertThat(paymentGroups).isNotNull();

        String newGroupName = "New group name";
        PaymentGroup newPaymentGroup = new PaymentGroup();
        newPaymentGroup.setName(newGroupName);
        newPaymentGroup = paymentGroupRepository.save(newPaymentGroup);

        assertThat(newPaymentGroup.getId()).isGreaterThan(0);

        List<PaymentGroup> findByName = paymentGroupRepository.findByName(newGroupName);
        assertThat(findByName.size()).isEqualTo(1);

        List<PaymentGroup> findByName2 = paymentGroupRepository.findByName(newGroupName + "added");
        assertThat(findByName2.size()).isEqualTo(0);

    }
}
