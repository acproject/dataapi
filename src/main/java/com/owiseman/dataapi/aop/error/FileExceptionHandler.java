package com.owiseman.dataapi.aop.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class FileExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "File not found"));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "File processing error"));
    }
}
