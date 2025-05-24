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

    // Thrown when we try to add a resource which does not satisfy constraints
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
    // Thrown when we delete, update a resource which does not exist
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }
    // Thrown when we add a resource which already exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> myResourceAlreadyExistsException(ResourceAlreadyExistsException e){
        String message = e.getMessage();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }
    // Thrown when we try to get a resource which does not have any values
    @ExceptionHandler(EmptyResourceException.class)
    public ResponseEntity<String> myEmptyResourceException(EmptyResourceException e){
        String message = e.getMessage();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
