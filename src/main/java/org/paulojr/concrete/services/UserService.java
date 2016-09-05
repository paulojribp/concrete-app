package org.paulojr.concrete.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.paulojr.concrete.daos.UserDao;
import org.paulojr.concrete.exceptions.*;
import org.paulojr.concrete.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

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

        String compactJws = generateJwsToken(user);
        user.setToken( compactJws );
        dao.create(user);
    }

    @Transactional
    public User validate(User user) {
        User u = dao.findByEmail(user.getEmail());

        if (u == null){
            throw new UsernameNotFoundException("Usuário e/ou senha inválidos");
        } else if ( !validatePassword(user.getPassword(), u) ) {
            throw new UnauthorizedException("Usuário e/ou senha inválidos");
        }

        u.setLastLogin(Calendar.getInstance());
        dao.update(u);

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

        if (!isSessionValid(user)) {
            throw new InvalidSessionException();
        }

        return user;
    }

    @Transactional
    public void update(User user) {
        dao.update(user);
    }

    public boolean isSessionValid(User user) {
        LocalDateTime lastLogin = LocalDateTime.ofInstant(user.getLastLogin().toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();

        long fromLastLogin = lastLogin.until(now, ChronoUnit.MINUTES);

        if (fromLastLogin > LoginService.SESSION_TIMEOUT) {
            return false;
        }

        return true;
    }

    private String generateJwsToken(User user) {
        Key key = MacProvider.generateKey();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

}
