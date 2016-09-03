package org.paulojr.concrete.daos;

import org.paulojr.concrete.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager manager;

    public void create(User user) {
        manager.persist(user);
    }

}
