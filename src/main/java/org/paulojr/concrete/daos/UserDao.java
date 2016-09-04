package org.paulojr.concrete.daos;

import org.paulojr.concrete.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager manager;

    public void create(User user) {
        manager.persist(user);
    }

    public User findByEmail(String email) {
        String jpql = "select u from User u where u.email = :email";
        try {
            return manager.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
