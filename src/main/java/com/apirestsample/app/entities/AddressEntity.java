package com.apirestsample.app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    private String id;
    @Column
    private String customer_id;
    @Column
    private String street;
    @Column
    private Integer num;
    @Column
    private String district;
    @Column
    private String city;
    @Column
    private String country;
    @Column
    private String zipcode;

}
