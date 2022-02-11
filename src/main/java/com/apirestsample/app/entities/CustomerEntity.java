package com.apirestsample.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "allowme_fake")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    private String username;
    @Column
    private String name;
    @Column
    private String sms;
    @Column
    private String smsDeviceId;
    @Column
    private String mail;
    @Column
    private String mailDeviceId;
    @Column
    private String active;

}
