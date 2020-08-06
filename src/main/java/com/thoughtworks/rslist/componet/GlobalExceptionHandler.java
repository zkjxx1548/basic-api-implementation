package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.exception.StartOrEndNotValidException;
import com.thoughtworks.rslist.exception.UserNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class, StartOrEndNotValidException.class, UserNotValidException.class})
    public ResponseEntity<Error> rsEven(Exception e) {

        String errorMessage;
        if (e instanceof RsEventNotValidException) {
            errorMessage = "invalid index";
        } else if (e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid param";
        } else if (e instanceof StartOrEndNotValidException) {
            errorMessage = "invalid request param";
        } else {
            errorMessage = "invalid user";
        }
        Error error = new Error();
        error.setError(errorMessage);
        LOGGER.error(error.getError());

        return ResponseEntity.badRequest().body(error);
    }
}
