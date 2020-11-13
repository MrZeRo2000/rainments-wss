package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentGroup;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entity.Product;
import com.romanpulov.rainmentswss.repository.PaymentGroupRepository;
import com.romanpulov.rainmentswss.repository.PaymentObjectRepository;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.repository.ProductRepository;
import com.romanpulov.rainmentswss.service.PaymentObjectPaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServicePaymentObjectPaymentTest {
    private static final Logger log = Logger.getLogger(ServicePaymentGroupTests.class.getName());

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentObjectRepository paymentObjectRepository;

    @Autowired
    private PaymentGroupRepository paymentGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentObjectPaymentService paymentObjectPaymentService;

    @BeforeAll
    static void prepareTestDB() {
        DBHelper.prepareTestDB();
    }

    private final LocalDate currentDate = LocalDate.now();
    LocalDate currentMonthDate = currentDate.withDayOfMonth(1);
    LocalDate previousMonthDate = currentDate.minusMonths(1L).withDayOfMonth(1);

    public ServicePaymentObjectPaymentTest() {
        log.info("ServicePaymentObjectPaymentTest constructor");
    }

    private void prepareTestData() {
        //empty payment object
        PaymentObject emptyPaymentObject = new PaymentObject();
        emptyPaymentObject.setName("Empty payment object");
        paymentObjectRepository.save(emptyPaymentObject);

        //payment object
        PaymentObject defaultPaymentObject = new PaymentObject();
        defaultPaymentObject.setName("Month Default");
        defaultPaymentObject.setPeriod("M");
        paymentObjectRepository.save(defaultPaymentObject);

        //payment object month next
        PaymentObject nextPaymentObject = new PaymentObject();
        nextPaymentObject.setName("Month Next");
        nextPaymentObject.setPeriod("M");
        nextPaymentObject.setPayDelay(1L);
        paymentObjectRepository.save(nextPaymentObject);

        //payment object month current
        PaymentObject currentPaymentObject = new PaymentObject();
        currentPaymentObject.setName("Month Current");
        currentPaymentObject.setPeriod("M");
        currentPaymentObject.setPayDelay(0L);
        paymentObjectRepository.save(currentPaymentObject);

        //payment group
        PaymentGroup newPaymentGroup = new PaymentGroup();
        newPaymentGroup.setName("New Payment Group");
        paymentGroupRepository.save(newPaymentGroup);

        //product
        Product newProduct = new Product();
        newProduct.setName("New Product Name");
        productRepository.save(newProduct);

        //payment
        Payment newPayment = new Payment();
        newPayment.setPaymentDate(LocalDate.now());
        newPayment.setPaymentPeriodDate(previousMonthDate);
        newPayment.setPaymentObject(defaultPaymentObject);
        newPayment.setPaymentGroup(newPaymentGroup);
        newPayment.setProduct(newProduct);
        newPayment.setPaymentAmount(BigDecimal.valueOf(14.22));
        newPayment.setCommissionAmount(BigDecimal.ZERO);
        paymentRepository.save(newPayment);
    }

    @Test
    void mainTest() throws Exception {
        prepareTestData();

        PaymentObject monthDefaultPaymentObject =  paymentObjectRepository.
                findAllByOrderByOrderIdAsc().
                stream().
                filter(po -> po.getName().equals("Month Default")).
                findFirst().
                orElseThrow(() -> new Exception("Month Default not found"));

        PaymentObject emptyPaymentObject =  paymentObjectRepository.
                findAllByOrderByOrderIdAsc().
                stream().
                filter(po -> po.getName().equals("Empty payment object")).
                findFirst().
                orElseThrow(() -> new Exception("Empty payment object not found"));

        Assertions.assertEquals(
                BigDecimal.valueOf(14.22),
                paymentObjectPaymentService.getTotalByPaymentObjectAndPaymentPeriod(
                        monthDefaultPaymentObject,
                        previousMonthDate
                ));

        Assertions.assertEquals(
                BigDecimal.ZERO,
                paymentObjectPaymentService.getTotalByPaymentObjectAndPaymentPeriod(
                        emptyPaymentObject,
                        previousMonthDate
                ));

    }

}
