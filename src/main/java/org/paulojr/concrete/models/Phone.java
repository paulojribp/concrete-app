package org.paulojr.concrete.models;

import javax.persistence.Embeddable;

@Embeddable
public class Phone {

    private String number;
    private String ddd;

    public Phone() {}

    public Phone(String ddd, String number) {
        this.ddd = ddd;
        this.number = number;
    }

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
