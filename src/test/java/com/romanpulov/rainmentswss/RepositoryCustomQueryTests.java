package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.repository.CustomQueryRepository;
import com.romanpulov.rainmentswss.service.PaymentGroupService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryCustomQueryTests {

    private static final Logger log = Logger.getLogger(RepositoryCustomQueryTests.class.getName());

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    @Autowired
    private CustomQueryRepository customQueryRepository;

    @Autowired
    private PaymentGroupService paymentGroupService;

    @Test
    void mainTest() {
        Long result;

        result = customQueryRepository.getMaxOrderId("PaymentGroup");
        Assertions.assertThat(result).isNull();

        result = customQueryRepository.getLongScalarQueryResult("SELECT p.id FROM PaymentGroup p");
        Assertions.assertThat(result).isNull();

        result = customQueryRepository.getMaxOrderId("PaymentGroup");
        Assertions.assertThat(result).isNull();

        // create groups
        paymentGroupService.save(createNew("Group 1"));
        paymentGroupService.save(createNew("Group 2"));
        paymentGroupService.save(createNew("Group 3"));

        result = customQueryRepository.getLongScalarQueryResult("SELECT p.id FROM PaymentGroup p");
        Assertions.assertThat(result).isNotNull();

        result = customQueryRepository.getLongScalarQueryResult("SELECT p.id FROM PaymentGroup p");
        Assertions.assertThat(result).isNotNull();

        result = customQueryRepository.getMaxOrderId("PaymentGroup");
        Assertions.assertThat(result).isNull();

        int rowsAffected = customQueryRepository.setDefaultOrder("PaymentGroup");
        Assertions.assertThat(rowsAffected).isEqualTo(3);

        result = customQueryRepository.getMaxOrderId("PaymentGroup");
        Assertions.assertThat(result).isEqualTo(3);

        List<PaymentGroup> paymentGroupList = new ArrayList<>();
        paymentGroupService.findAll().forEach(paymentGroupList::add);
        Assertions.assertThat(paymentGroupList.size()).isEqualTo(3);
        Assertions.assertThat(paymentGroupList.get(0).getOrderId()).isEqualTo(1);
        Assertions.assertThat(paymentGroupList.get(1).getOrderId()).isEqualTo(2);
        Assertions.assertThat(paymentGroupList.get(2).getOrderId()).isEqualTo(3);

        customQueryRepository.moveOrder(
                "PaymentGroup",
                paymentGroupList.get(0).getId(),
                paymentGroupList.get(0).getOrderId(),
                paymentGroupList.get(2).getId(),
                paymentGroupList.get(2).getOrderId()
                );

        paymentGroupList = new ArrayList<>();
        paymentGroupService.findAll().forEach(paymentGroupList::add);
        Assertions.assertThat(paymentGroupList.get(0).getOrderId()).isEqualTo(2);
    }

    private PaymentGroup createNew(String groupName) {
        PaymentGroup result = new PaymentGroup();
        result.setName(groupName);
        return result;
    }
}
