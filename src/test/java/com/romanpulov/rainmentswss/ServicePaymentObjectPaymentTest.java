package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.dto.PaymentObjectPeriodTotalDTO;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServicePaymentObjectPaymentTest {
    private static final Logger log = Logger.getLogger(ServicePaymentGroupTests.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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

    private final LocalDate currentDate = LocalDate.now().withDayOfMonth(20);
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

        //payment object month next term 4 days
        PaymentObject next4DaysPaymentObject = new PaymentObject();
        next4DaysPaymentObject.setName("Month Next term 4 days");
        next4DaysPaymentObject.setPeriod("M");
        next4DaysPaymentObject.setPayDelay(1L);
        next4DaysPaymentObject.setTerm("4D");
        paymentObjectRepository.save(next4DaysPaymentObject);

        //payment object quarter current term 10 days
        PaymentObject current10DaysQPaymentObject = new PaymentObject();
        current10DaysQPaymentObject.setName("Quarter Next term 10 days");
        current10DaysQPaymentObject.setPeriod("Q");
        current10DaysQPaymentObject.setPayDelay(0L);
        current10DaysQPaymentObject.setTerm("10D");
        paymentObjectRepository.save(current10DaysQPaymentObject);

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

        //payment
        Payment newPaymentMN4D = new Payment();
        newPaymentMN4D.setPaymentDate(currentDate);
        newPaymentMN4D.setPaymentPeriodDate(previousMonthDate);
        newPaymentMN4D.setPaymentObject(next4DaysPaymentObject);
        newPaymentMN4D.setPaymentGroup(newPaymentGroup);
        newPaymentMN4D.setProduct(newProduct);
        newPaymentMN4D.setPaymentAmount(BigDecimal.valueOf(78.32));
        newPaymentMN4D.setCommissionAmount(BigDecimal.ZERO);
        paymentRepository.save(newPaymentMN4D);

        //payment quarter current
        Payment newPaymentQC10D = new Payment();
        newPaymentQC10D.setPaymentDate(currentDate);
        newPaymentQC10D.setPaymentPeriodDate(LocalDate.parse("01.04.2003", formatter));
        newPaymentQC10D.setPaymentObject(current10DaysQPaymentObject);
        newPaymentQC10D.setPaymentGroup(newPaymentGroup);
        newPaymentQC10D.setProduct(newProduct);
        newPaymentQC10D.setPaymentAmount(BigDecimal.valueOf(4.55));
        newPaymentQC10D.setCommissionAmount(BigDecimal.ZERO);
        paymentRepository.save(newPaymentQC10D);

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
                BigDecimal.valueOf(14.22),
                paymentObjectPaymentService.getTotalAmountByPaymentObjectAndPaymentPeriod(
                        monthDefaultPaymentObject,
                        previousMonthDate
                ));

        Assertions.assertEquals(
                BigDecimal.ZERO,
                paymentObjectPaymentService.getTotalByPaymentObjectAndPaymentPeriod(
                        emptyPaymentObject,
                        previousMonthDate
                ));

        Assertions.assertEquals(
                BigDecimal.ZERO,
                paymentObjectPaymentService.getTotalAmountByPaymentObjectAndPaymentPeriod(
                        emptyPaymentObject,
                        previousMonthDate
                ));

        List<PaymentObjectPeriodTotalDTO> total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(currentDate);
        Assertions.assertNotNull(total);

        Assertions.assertEquals(BigDecimal.ZERO, total.get(0).getPaymentAmount());
        Assertions.assertEquals(BigDecimal.valueOf(14.22), total.get(1).getPaymentAmount());
        Assertions.assertEquals(BigDecimal.ZERO, total.get(2).getPaymentAmount());
        Assertions.assertEquals(BigDecimal.ZERO, total.get(3).getPaymentAmount());
        Assertions.assertEquals(BigDecimal.valueOf(78.32), total.get(4).getPaymentAmount());

        Assertions.assertFalse(total.get(0).getPaymentOverdue());
        Assertions.assertFalse(total.get(1).getPaymentOverdue());
        Assertions.assertFalse(total.get(2).getPaymentOverdue());
        Assertions.assertFalse(total.get(3).getPaymentOverdue());
        Assertions.assertFalse(total.get(4).getPaymentOverdue());

        //next month
        total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(currentDate.plusMonths(1L));
        Assertions.assertNotNull(total);

        Assertions.assertTrue(total.get(4).getPaymentOverdue());

        // for quarter
        total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(LocalDate.parse("05.04.2003", formatter));
        Assertions.assertFalse(total.get(5).getPaymentOverdue());

        total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(LocalDate.parse("25.04.2003", formatter));
        Assertions.assertFalse(total.get(5).getPaymentOverdue());

        total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(LocalDate.parse("02.05.2003", formatter));
        Assertions.assertFalse(total.get(5).getPaymentOverdue());

        total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(LocalDate.parse("02.07.2003", formatter));
        Assertions.assertFalse(total.get(5).getPaymentOverdue());

        total = paymentObjectPaymentService.getPaymentObjectPeriodTotal(LocalDate.parse("25.07.2003", formatter));
        Assertions.assertTrue(total.get(5).getPaymentOverdue());
    }

}
