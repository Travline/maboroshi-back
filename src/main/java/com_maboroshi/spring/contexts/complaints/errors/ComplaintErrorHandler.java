package com_maboroshi.spring.contexts.complaints.errors;

import com_maboroshi.spring.shared.utils.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@RequiredArgsConstructor
public class ComplaintErrorHandler {

    private final Slf4jLogger logger;

    @ExceptionHandler(CannotCreateComplaint.class)
    public ResponseEntity<HashMap<String, String>> handleCannotCreateComplaint(
            CannotCreateComplaint ex) {

        var response = new HashMap<String, String>();

        response.put("error", ex.getMessage());

        logger.warn(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}