package org.wrabz.note.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.ok(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Map<String, String >> myMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> response = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error ->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            response.put(fieldName, message);
        });

        return ResponseEntity.ok(response);
    }
}
