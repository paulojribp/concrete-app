package org.paulojr.concrete.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Usuario {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private List<Phone> phones;



}
