package org.paulojr.concrete.test.builders;

import org.paulojr.concrete.models.Phone;
import org.paulojr.concrete.models.User;

import java.util.ArrayList;
import java.util.Arrays;

public class UserBuilder {

    private User user = new User();

    private UserBuilder() {
        this.user = new User();
        this.user.setPhones(new ArrayList<>());
    }

    public static UserBuilder newInstance() {
        return new UserBuilder();
    }

    public User build() {
        return this.user;
    }

    public UserBuilder name(String name) {
        this.user.setName(name);
        return this;
    }

    public UserBuilder sampleUser() {
        user = new User();
        user.setName("Davi");
        user.setEmail("davi@gmail.com");
        user.setPassword("davi@2016");
        Phone phone = new Phone();
        phone.setDdd("11");
        phone.setNumber("948372573");
        user.setPhones(Arrays.asList(phone));

        return this;
    }

    public UserBuilder password(String password) {
        this.user.setPassword(password);
        return this;
    }

    public UserBuilder email(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder phone(String ddd, String number) {
        this.user.getPhones().add(new Phone(ddd, number));
        return this;
    }
}
