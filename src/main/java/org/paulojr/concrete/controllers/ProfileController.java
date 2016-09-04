package org.paulojr.concrete.controllers;

import org.paulojr.concrete.models.User;
import org.paulojr.concrete.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> read(@PathVariable String id, @RequestHeader(value = "token") String token) {
        User user = userService.validateUserByToken(id, token);

        return ResponseEntity.ok(user);
    }

}
