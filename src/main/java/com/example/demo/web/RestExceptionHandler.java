package com.example.demo.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;

/**
 * Ответы об ошибках в формате RFC 7807 (Problem Details for HTTP APIs) —
 * фиксированный набор полей: type, title, status, detail, instance.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /** Стабильный идентификатор класса проблемы для клиентов (URN/URL). */
    private static final URI TYPE_ENDPOINT_NOT_FOUND = URI.create("urn:problem-type:endpoint-not-found");
    private static final URI TYPE_INVALID_REQUEST = URI.create("urn:problem-type:invalid-request");
    private static final URI TYPE_RESOURCE_NOT_FOUND = URI.create("urn:problem-type:resource-not-found");

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Endpoint not found: " + ex.getRequestURL()
        );
        body.setType(TYPE_ENDPOINT_NOT_FOUND);
        body.setTitle("Endpoint not found");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String detail = "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue();
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        body.setType(TYPE_INVALID_REQUEST);
        body.setTitle("Invalid request");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        body.setType(TYPE_RESOURCE_NOT_FOUND);
        body.setTitle("Resource not found");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }
}
