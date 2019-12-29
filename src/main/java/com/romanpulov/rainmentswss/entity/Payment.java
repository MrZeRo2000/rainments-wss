package com.romanpulov.rainmentswss.entity;

import javax.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
