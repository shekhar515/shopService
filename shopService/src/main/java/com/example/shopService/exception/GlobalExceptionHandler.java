package com.example.shopService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(RateLimitException.class)
 public ResponseEntity<ErrorResponse> handleRateLimitException(RateLimitException ex){
   ErrorResponse error =new ErrorResponse();
   error.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
   error.setMessage(ex.getMessage());
   error.setTimestamp(System.currentTimeMillis());

   
    return new  ResponseEntity<>(error,HttpStatus.TOO_MANY_REQUESTS);

 }
 //shop id did not exist
 @ExceptionHandler(RuntimeException.class)
 public ResponseEntity<ErrorResponse> handleGeneralException(RuntimeException ex){
    ErrorResponse error = new ErrorResponse();
   error.setStatus(HttpStatus.BAD_REQUEST.value());
   error.setMessage(ex.getMessage());
   error.setTimestamp(System.currentTimeMillis());
    
    
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
 }
 //shop not found
 @ExceptionHandler(ShopNotFoundException.class)
public ResponseEntity<ErrorResponse> handleShopNotFound(ShopNotFoundException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setStatus(HttpStatus.NOT_FOUND.value()); // 404
    error.setMessage(ex.getMessage());
    error.setTimestamp(System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}

//product not found
@ExceptionHandler(ProductNotFoundException.class)
public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setStatus(HttpStatus.NOT_FOUND.value()); // 404
    error.setMessage(ex.getMessage());
    error.setTimestamp(System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}
}
