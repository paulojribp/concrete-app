package org.paulojr.concrete.services;

import org.paulojr.concrete.daos.UserDao;
import org.paulojr.concrete.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao dao;

    @Transactional
    public void create(User user) {
        dao.create(user);
    }

}
