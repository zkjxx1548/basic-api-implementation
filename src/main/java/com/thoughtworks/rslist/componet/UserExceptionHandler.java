package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.StartOrEndNotValidException;
import com.thoughtworks.rslist.exception.UserNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<Error> inValidUser(Exception e) {
        String errorMessage = "invalid user";
        Error error = new Error();
        error.setError(errorMessage);

        return ResponseEntity.badRequest().body(error);
    }
}
