package org.paulojr.concrete.controllers;

import org.paulojr.concrete.models.User;
import org.paulojr.concrete.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<User> singIn(@RequestBody User user) {
        user = loginService.validate(user);

        return ResponseEntity.ok(user);
    }

}
