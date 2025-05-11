package com.ecommerce.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
// Tells all exceptions occuring in this springboot context will be handled in this class
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // This method will handle all exceptions for MethodArgumentNotValidException type
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> response = new HashMap<>();
        // BindingResult method gives list of all errors caught during given exception
        e.getBindingResult().getAllErrors().forEach((error)->{
           String fieldName = ((FieldError)error).getField();
           String errorMessage = error.getDefaultMessage();
           response.put(fieldName,errorMessage);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
