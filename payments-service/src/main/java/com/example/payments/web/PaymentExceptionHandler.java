package com.example.payments.web;

import com.example.payments.exception.PaymentValidationException;
import com.example.payments.exception.UpstreamServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;

@RestControllerAdvice
public class PaymentExceptionHandler {
    private static final URI TYPE_ENDPOINT_NOT_FOUND = URI.create("urn:problem-type:endpoint-not-found");
    private static final URI TYPE_INVALID_REQUEST = URI.create("urn:problem-type:invalid-request");
    private static final URI TYPE_PAYMENT_VALIDATION = URI.create("urn:problem-type:payment-validation");
    private static final URI TYPE_PRODUCTS_SERVICE_ERROR = URI.create("urn:problem-type:products-service-error");

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Endpoint not found: " + ex.getRequestURL());
        body.setType(TYPE_ENDPOINT_NOT_FOUND);
        body.setTitle("Endpoint not found");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ProblemDetail> handleInvalidRequest(Exception ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        body.setType(TYPE_INVALID_REQUEST);
        body.setTitle("Invalid request");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);
    }

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<ProblemDetail> handlePaymentValidation(PaymentValidationException ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        body.setType(TYPE_PAYMENT_VALIDATION);
        body.setTitle("Payment validation failed");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);
    }

    @ExceptionHandler(UpstreamServiceException.class)
    public ResponseEntity<ProblemDetail> handleProductsServiceError(UpstreamServiceException ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());
        body.setType(TYPE_PRODUCTS_SERVICE_ERROR);
        body.setTitle("Products service error");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(body);
    }
}
