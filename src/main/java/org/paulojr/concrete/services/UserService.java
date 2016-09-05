package org.paulojr.concrete.services;

import org.paulojr.concrete.daos.UserDao;
import org.paulojr.concrete.exceptions.EmailAlreadyUsedException;
import org.paulojr.concrete.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao dao;

    @Autowired
    private LoginService loginService;

    @Transactional
    public void create(User user) {
        if (userExists(user)) {
            throw new EmailAlreadyUsedException();
        }
        user.setPassword(loginService.encode(user.getPassword()));

        String compactJws = loginService.generateJwsToken(user);
        user.setToken( compactJws );
        dao.create(user);
    }

    public boolean userExists(User user) {
        user = dao.findByEmail(user.getEmail());

        return user != null;
    }

    @Transactional
    public void update(User user) {
        dao.update(user);
    }

}
