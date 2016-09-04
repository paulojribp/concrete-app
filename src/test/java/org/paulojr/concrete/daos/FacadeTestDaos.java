package org.paulojr.concrete.daos;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class FacadeTestDaos {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public boolean clearUsers() {
        int executed = manager.createNativeQuery("delete from User_phones")
                .executeUpdate();
        if (executed > 0) {
            executed = manager.createNativeQuery("delete from Users")
                    .executeUpdate();
        }

        return executed > 0;
    }

}
