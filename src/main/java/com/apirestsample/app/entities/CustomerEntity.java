package com.apirestsample.app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String active;

}
