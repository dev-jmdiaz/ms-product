package com.inditex.infrastructure.exception;

import com.inditex.application.dto.ErrorResponseDTO;
import com.inditex.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;

/**
 * Global exception handler for handling various exceptions in a centralized manner.
 * <p>
 * This class intercepts exceptions thrown by controllers and returns appropriate error responses.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles {@link NotFoundException} and returns a 404 NOT FOUND response.
     *
     * @param ex the exception
     * @param request the server web exchange
     * @return a {@link ResponseEntity} with error details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NotFoundException ex, ServerWebExchange request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles exceptions such as {@link IllegalArgumentException},
     * {@link MethodArgumentTypeMismatchException}, and {@link NoResourceFoundException},
     * returning a 400 BAD REQUEST response.
     *
     * @param ex the exception
     * @param request the server web exchange
     * @return a {@link ResponseEntity} with error details
     */
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class, NoResourceFoundException.class})
    public ResponseEntity<Object> handleIllegalArgument(Exception ex, ServerWebExchange request) {
        log.error("exception : {}",ex.getMessage());
        var badRequest = HttpStatus.BAD_REQUEST;
        return buildErrorResponse(badRequest, badRequest.getReasonPhrase());
    }

    /**
     * Handles general exceptions and returns a 500 INTERNAL SERVER ERROR response.
     *
     * @param ex the exception
     * @param request the server web exchange
     * @return a {@link ResponseEntity} with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, ServerWebExchange request) {
        log.error("exception : {}",ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    /**
     * Handles missing request parameters and returns a 400 BAD REQUEST response.
     *
     * @param ex the exception
     * @return a {@link ResponseEntity} with error details
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                String.format("The required query parameter '%s' is missing.", ex.getParameterName()));
    }

    /**
     * Builds a standardized error response with status, message, and timestamp.
     *
     * @param status the HTTP status
     * @param message the error message
     * @return a {@link ResponseEntity} containing the error details
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {

        log.error("exception : {}", message);
        var body = new ErrorResponseDTO();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(status.value());
        body.setError(status.getReasonPhrase());
        body.setMessage(message);
        return new ResponseEntity<>(body, status);


    }
}
