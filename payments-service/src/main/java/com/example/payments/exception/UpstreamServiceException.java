package com.example.payments.exception;

import org.springframework.http.HttpStatusCode;

public class UpstreamServiceException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public UpstreamServiceException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
