package org.paulojr.concrete.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.paulojr.concrete.daos.UserDao;
import org.paulojr.concrete.exceptions.EmailAlreadyUsedException;
import org.paulojr.concrete.exceptions.InvalidTokenException;
import org.paulojr.concrete.exceptions.NotAuthorizedException;
import org.paulojr.concrete.exceptions.TokenNotFoundException;
import org.paulojr.concrete.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;

@Service
public class UserService {

    @Autowired
    private UserDao dao;

    @Transactional
    public void create(User user) {
        if (userExists(user)) {
            throw new EmailAlreadyUsedException();
        }
        user.setPassword(encode(user.getPassword()));

        String compactJws = generateToken(user);
        user.setToken( compactJws );

//        Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject()

        dao.create(user);
    }

    private String generateToken(User user) {
        Key key = MacProvider.generateKey();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
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

    public User validateUserByToken(String id, String token) {
        if (!dao.tokenExists(token)) {
            throw new TokenNotFoundException();
        }

        User user = dao.findById(id);

        if (user == null || !token.equals(user.getToken())) {
            throw new InvalidTokenException();
        }

        return user;
    }
}
