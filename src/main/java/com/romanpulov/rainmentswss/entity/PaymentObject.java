package com.romanpulov.rainmentswss.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "payment_objects")
public class PaymentObject {

    @Id
    @Column(name = "payment_object_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(name = "payment_object_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PaymentObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentObject paymentObject = (PaymentObject) o;
        return id.equals(paymentObject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}