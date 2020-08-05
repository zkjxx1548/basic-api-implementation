package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.exception.StartOrEndNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Error> rsEven(Exception e) {
        String errorMessage;
        if (e instanceof RsEventNotValidException) {
            errorMessage = "invalid index";
        } else {
            errorMessage = "invalid param";
        }
        Error error = new Error();
        error.setError(errorMessage);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(StartOrEndNotValidException.class)
    public ResponseEntity<Error> inValidIndexOfStartOrEnd(Exception e) {
        String errorMessage = "invalid request param";
        Error error = new Error();
        error.setError(errorMessage);

        return ResponseEntity.badRequest().body(error);
    }
}
