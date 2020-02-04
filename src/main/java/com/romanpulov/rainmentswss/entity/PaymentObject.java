package com.romanpulov.rainmentswss.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "payment_objects")
public class PaymentObject extends PaymentDictionarySuperclass{

    public PaymentObject() {
        super();
    }

    @Id
    @Column(name = "payment_object_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty
    @Column(name = "payment_object_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "paymentObject", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();

    @PreRemove
    private void preRemove() {
        if (payments.size() > 0) {
            throw new RuntimeException("Unable to delete " + this + " because it has child payments");
        }
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
