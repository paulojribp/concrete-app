package org.paulojr.concrete.controllers;

import org.paulojr.concrete.exceptions.Message;
import org.paulojr.concrete.exceptions.NotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Message> catchException(Exception e) {
        return ResponseEntity.badRequest()
                .body(new Message(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Message> catchUserNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Message(e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Message> catchUserNotFoundException(NotAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Message(e.getMessage()));
    }

}
