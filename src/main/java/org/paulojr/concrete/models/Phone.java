package org.paulojr.concrete.models;

import javax.persistence.Embeddable;

@Embeddable
public class Phone {

    private String number;
    private String ddd;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
