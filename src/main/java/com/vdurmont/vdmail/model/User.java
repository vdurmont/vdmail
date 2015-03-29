package com.vdurmont.vdmail.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "vd_user")
public class User extends Entity {
    @Column(length = 255)
    private String name;
    @org.hibernate.validator.constraints.Email
    @NotEmpty
    @Column(unique = true, nullable = false, length = 255)
    private String address;
    @Column(length = 255)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
