package org.paulojr.concrete.models;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.List;
import java.util.UUID;

@Entity
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    @ElementCollection
    private List<Phone> phones;

    @PrePersist
    public void generateId() {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

}
