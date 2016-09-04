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

    public boolean tokenExists(String token) {
        String jpql = "select count(u.token) from User u where u.token = :token";
        try {
            Long result = manager.createQuery(jpql, Long.class)
                    .setParameter("token", token)
                    .getSingleResult();

            return result == 1;
        } catch (NoResultException e) {
            return false;
        }
    }

    public User findById(String id) {
        return manager.find(User.class, id);
    }
}
