package com.romanpulov.rainmentswss.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "payment_groups")
public class PaymentGroup extends PaymentDictionarySuperclass{

    public PaymentGroup() {
        super();
    }

    @Id
    @Column(name = "payment_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @NotEmpty
    @Column(name = "payment_group_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "payment_group_url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_group_id", referencedColumnName = "payment_group_id")
    private Set<Payment> payments = new HashSet<>();

    @PreRemove
    private void preRemove() {
        if (payments.size() > 0) {
            throw new RuntimeException("Unable to delete " + this + " because it has child payments");
        }
    }

    @Override
    public String toString() {
        return "PaymentGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentGroup that = (PaymentGroup) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
