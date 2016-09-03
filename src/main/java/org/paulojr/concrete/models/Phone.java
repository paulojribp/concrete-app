package org.paulojr.concrete.models;

import javax.persistence.Embeddable;

@Embeddable
public class Phone {

    private String phone;
    private String ddd;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
