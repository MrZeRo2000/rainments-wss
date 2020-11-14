package com.romanpulov.rainmentswss.service;

import com.romanpulov.rainmentswss.dto.PaymentObjectPeriodTotalDTO;
import com.romanpulov.rainmentswss.entity.Payment;
import com.romanpulov.rainmentswss.entity.PaymentObject;
import com.romanpulov.rainmentswss.entity.converter.AmountConverter;
import com.romanpulov.rainmentswss.entitymapper.PaymentObjectDTOMapper;
import com.romanpulov.rainmentswss.repository.PaymentRepository;
import com.romanpulov.rainmentswss.vo.Period;
import com.romanpulov.rainmentswss.vo.PeriodType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PaymentObjectPaymentService {
    private final PaymentObjectService paymentObjectService;

    private final PaymentRepository paymentRepository;

    private final PaymentObjectDTOMapper paymentObjectDTOMapper;

    public PaymentObjectPaymentService(PaymentObjectService paymentObjectService, PaymentRepository paymentRepository, PaymentObjectDTOMapper paymentObjectDTOMapper) {
        this.paymentObjectService = paymentObjectService;
        this.paymentRepository = paymentRepository;
        this.paymentObjectDTOMapper = paymentObjectDTOMapper;
    }

    public Map<PaymentObject, BigDecimal> getPaymentObjectTotalsByPaymentPeriod(LocalDate paymentPeriodDate) {

        List<Payment> paymentList = paymentRepository.findAllByPaymentPeriodDate(paymentPeriodDate);

        Map<PaymentObject, BigDecimal> paymentListTotals =
                paymentList
                        .stream()
                        .collect(Collectors.groupingBy(
                                Payment::getPaymentObject,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Payment::getPaymentAmount,
                                        BigDecimal::add
                                )
                                )
                        );

        Map<PaymentObject, BigDecimal> result = new LinkedHashMap<>();

        paymentObjectService.findAll().forEach(paymentObject -> {
            BigDecimal value = paymentListTotals.get(paymentObject);
            result.put(paymentObject, Optional.ofNullable(value).orElse(BigDecimal.ZERO));
        });

        return result;
    }

    public BigDecimal getTotalByPaymentObjectAndPaymentPeriod(PaymentObject paymentObject, LocalDate paymentPeriodDate) {
        List<Payment> paymentList = paymentRepository.findByPaymentObjectIdAndPaymentPeriodDate(
                paymentObject,
                paymentPeriodDate,
                Sort.unsorted()
        );

        return paymentList.stream().map(Payment::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalAmountByPaymentObjectAndPaymentPeriod(PaymentObject paymentObject, LocalDate paymentPeriodDate) {
        BigDecimal repositoryResult = paymentRepository.sumByPaymentObjectIdAndPaymentPeriodDate(paymentObject, paymentPeriodDate);
        return repositoryResult == null ? BigDecimal.ZERO : repositoryResult.divide(AmountConverter.SCALE, MathContext.DECIMAL32);
    }

    public List<PaymentObjectPeriodTotalDTO> getPaymentObjectPeriodTotal(LocalDate currentDate) {
        List<PaymentObjectPeriodTotalDTO> result = new ArrayList<>();

        Pattern termPattern = Pattern.compile("(\\d+)?(\\w)");

        paymentObjectService.findAll().forEach(paymentObject -> {

            // calc delay
            long payDelay = paymentObject.getPayDelay() == null ? 1 : paymentObject.getPayDelay();

            // calc paymentDate and overdue
            LocalDate paymentDate = null;
            boolean paymentOverdue = false;

            if (paymentObject.getPeriod() != null) {
                try {
                    PeriodType paymentPeriodType = PeriodType.valueOf(paymentObject.getPeriod());
                    Period paymentPeriod = new Period(paymentPeriodType, -payDelay);

                    LocalDate currentDateTruncated = Period.truncateToPeriodType(currentDate, paymentPeriodType);

                    paymentDate = paymentPeriod.addToDate(currentDateTruncated);

                    // calc overdue
                    String paymentTerm = paymentObject.getTerm();
                    if (paymentTerm != null && !paymentTerm.isEmpty()) {
                        Period paymentTermPeriod = Period.fromString(paymentTerm);
                        if (paymentTermPeriod != null) {
                            LocalDate startDueDate = payDelay == 0 ? currentDateTruncated : paymentPeriod.addToDate(currentDateTruncated);
                            LocalDate dueDate = paymentTermPeriod.addToDate(startDueDate);
                            paymentOverdue = dueDate.isAfter(currentDate);
                        }
                    }


                } catch (IllegalArgumentException ignored) {
                }
            }

            // calc totalAmount
            BigDecimal totalAmount = (paymentDate == null) ? BigDecimal.ZERO :
                    getTotalAmountByPaymentObjectAndPaymentPeriod(paymentObject, paymentDate);

            result.add(new PaymentObjectPeriodTotalDTO(
                    paymentObjectDTOMapper.entityToDTO(paymentObject),
                    paymentDate,
                    totalAmount,
                    paymentOverdue
            ));

        });


        return result;
    }
}
