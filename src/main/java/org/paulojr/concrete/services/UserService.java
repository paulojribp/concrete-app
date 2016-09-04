package org.paulojr.concrete.services;

import org.paulojr.concrete.daos.UserDao;
import org.paulojr.concrete.exceptions.EmailAlreadyUsedException;
import org.paulojr.concrete.exceptions.NotAuthorizedException;
import org.paulojr.concrete.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao dao;

    @Transactional
    public void create(User user) {
        user.setPassword(encode(user.getPassword()));
        if (userExists(user)) {
            throw new EmailAlreadyUsedException();
        }

        dao.create(user);
    }

    public User validate(User user) {
        User u = dao.findByEmail(user.getEmail());

        if (u == null){
            throw new UsernameNotFoundException("Usuário e/ou senha inválidos");
        } else if ( !validatePassword(user.getPassword(), u) ) {
            throw new NotAuthorizedException("Usuário e/ou senha inválidos");
        }

        return u;
    }

    public boolean userExists(User user) {
        user = dao.findByEmail(user.getEmail());

        return user != null;
    }

    public String encode(String text) {
        return new BCryptPasswordEncoder().encode(text);
    }
    public boolean validatePassword(String simpleTextPassword, User user) {
        return new BCryptPasswordEncoder().matches(simpleTextPassword, user.getPassword());
    }

}
