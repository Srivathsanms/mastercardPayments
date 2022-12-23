package com.evaluation.mastercardPayments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setErrorCode(e.getCustomErrors());
        errorResponse.setErrorMessage(e.getCustomErrors().getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<CustomErrorResponse> handleHttpMethodException(HttpRequestMethodNotSupportedException e) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpNotReadableException(HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Invalid Request"), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Invalid " + exception.getBindingResult().getFieldError().getField()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleInvalidRequest(ConstraintViolationException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Invalid Request"), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
