package org.paulojr.concrete.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.paulojr.concrete.daos.UserDao;
import org.paulojr.concrete.exceptions.InvalidSessionException;
import org.paulojr.concrete.exceptions.InvalidTokenException;
import org.paulojr.concrete.exceptions.TokenNotFoundException;
import org.paulojr.concrete.exceptions.UnauthorizedException;
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
public class LoginService {

    public static final int SESSION_TIMEOUT = 30;

    @Autowired
    private UserDao userDao;

    @Transactional
    public User validate(User user) {
        User u = userDao.findByEmail(user.getEmail());

        if (u == null){
            throw new UsernameNotFoundException("Usuário e/ou senha inválidos");
        } else if ( !validatePassword(user.getPassword(), u) ) {
            throw new UnauthorizedException("Usuário e/ou senha inválidos");
        }

        u.setLastLogin(Calendar.getInstance());
        userDao.update(u);

        return u;
    }

    public String encode(String text) {
        return new BCryptPasswordEncoder().encode(text);
    }

    public boolean validatePassword(String simpleTextPassword, User user) {
        return new BCryptPasswordEncoder().matches(simpleTextPassword, user.getPassword());
    }

    public User validateUserByToken(String id, String token) {
        if (!userDao.tokenExists(token)) {
            throw new TokenNotFoundException();
        }

        User user = userDao.findById(id);

        if (user == null || !token.equals(user.getToken())) {
            throw new InvalidTokenException();
        }

        if (!isSessionValid(user)) {
            throw new InvalidSessionException();
        }

        return user;
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

    String generateJwsToken(User user) {
        Key key = MacProvider.generateKey();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

}
