package org.paulojr.concrete.controllers;

import org.paulojr.concrete.exceptions.InvalidTokenException;
import org.paulojr.concrete.exceptions.Message;
import org.paulojr.concrete.exceptions.NotAuthorizedException;
import org.paulojr.concrete.exceptions.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> catchException(Exception e) {
        return ResponseEntity.badRequest()
                .body(new Message(e.getMessage()));
    }

    @ExceptionHandler({UsernameNotFoundException.class, TokenNotFoundException.class})
    public ResponseEntity<Message> catchUserNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Message(e.getMessage()));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Message> catchUserNotFoundException(NotAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Message(e.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Message> catchUserNotFoundException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Message(e.getMessage()));
    }

}
