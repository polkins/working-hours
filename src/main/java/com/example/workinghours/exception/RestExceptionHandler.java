package com.example.workinghours.exception;

import com.example.workinghours.dto.ExceptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class, HttpMessageNotReadableException.class
    })
    public ResponseEntity<ExceptionDTO> handle(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO().setMessage(
                "Please, try to fix your input data: \n" + ex.getMessage()
        ));
    }
}
