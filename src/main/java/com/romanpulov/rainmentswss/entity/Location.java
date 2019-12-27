package com.romanpulov.rainmentswss.entity;

import javax.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(name = "location_id")
    @GeneratedValue
    private Long id;

    @Column(name = "location_name")
    private String name;
}
