package com.foodtech.foodtechstore.base.controller;


import com.foodtech.foodtechstore.admin.exception.AdminExistException;
import com.foodtech.foodtechstore.admin.exception.AdminNotExistException;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.base.api.response.ErrorResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandleApiExceptions extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Object> notFoundException(ChangeSetPersister.NotFoundException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("NotFoundException", HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(AdminExistException.class)
    public ResponseEntity<Object> userExistException(AdminExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("AdminExistException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> badRequest(AdminNotExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("ResponseStatusException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AdminNotExistException.class)
    public ResponseEntity<Object> userNotExistException(AdminNotExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("AdminNotExistException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> authException(AuthException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("AuthException", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(NotAccessException.class)
    public ResponseEntity<Object> notAccessException(NotAccessException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("NotAccessException", HttpStatus.FORBIDDEN));
    }






    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("Exception", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
