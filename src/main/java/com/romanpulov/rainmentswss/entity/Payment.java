package com.romanpulov.rainmentswss.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date")
    @Convert(converter = DateConverter.class)
    private LocalDate paymentDate;

    @Column(name = "payment_period_date")
    @Convert(converter = DateConverter.class)
    private LocalDate paymentPeriodDate;

    @Column(name = "payment_object_id")
    private Long paymentObjectId;

    @Column(name = "payment_group_id")
    private Long paymentGroupId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_counter")
    private String productCounter;

}
