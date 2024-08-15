package org.mindera.fur.code.aspect;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.mindera.fur.code.exceptions.donation.DonationNotFoundException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationAmountException;
import org.mindera.fur.code.exceptions.donation.InvalidDonationDateException;
import org.mindera.fur.code.exceptions.file.FileException;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.exceptions.token.TokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

import static org.springframework.http.HttpStatus.*;


@Component
@ControllerAdvice
public class ExceptionAspect extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    /**
     * Handles method not supported exceptions, returning a 405 response with the appropriate error message.
     *
     * @param ex      the exception
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error("{}: {}", "Method Not Supported", ex.getMessage());
        String responseJson = response(
                status.value(),
                status.toString(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "The endpoint " + ((ServletWebRequest) request).getRequest().getRequestURI() + " does not support the method " + ((ServletWebRequest) request).getHttpMethod() + ".",
                ex.getMessage(),
                new Date());
        return new ResponseEntity<>(responseJson, HttpStatusCode.valueOf(status.value()));
    }

    /**
     * Handles resource not found exceptions, returning a 404 response with the appropriate error message.
     *
     * @param ex      the exception
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error("{}: {}", "Endpoint Not Found", ex.getMessage());
        String responseJson = response(
                status.value(),
                status.toString(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "The endpoint " + ((ServletWebRequest) request).getRequest().getRequestURI() + " does not exist.",
                ex.getMessage(),
                new Date());
        return new ResponseEntity<>(responseJson, HttpStatusCode.valueOf(status.value()));
    }

    /**
     * Handles method argument type mismatch exceptions, returning a 400 response with the appropriate error message.
     *
     * @param ex      the exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.error("{}: {}", "A bad request was made", ex.getMessage());
        String responseJson = response(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "A bad request was made.",
                "'" + ex.getName() + "' should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName(),
                new Date());

        return new ResponseEntity<>(responseJson, BAD_REQUEST);
    }

    /**
     * Handles HTTP media type not supported exceptions, returning a 415 response with the appropriate error message.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error("{}: {}", "The media type received is not supported", ex.getMessage());
        String responseJson = response(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "The media type received is not supported.",
                "'" + ex.getContentType() + "' is not supported.",
                new Date());

        return new ResponseEntity<>(responseJson, UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handles HTTP message not readable exceptions, returning a 400 response with the appropriate error message.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error("{}: {}", "Cannot parse the request body", ex.getMessage());
        String responseJson = response(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "Cannot parse the request body.",
                "If JSON was send, check the formatting.",
                new Date());

        return new ResponseEntity<>(responseJson, BAD_REQUEST);
    }

    /**
     * Handles not found exceptions, returning a 404 response with the appropriate error message.
     *
     * @param e       the exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler({
            NoSuchElementException.class,
            DonationNotFoundException.class,
            EntityNotFoundException.class,
//            AdoptionFormNotFound.class,
    })
    public ResponseEntity<String> ResourceNotFoundException(Exception e, HttpServletRequest request) {
        logger.error("{}: {}", "Resource Not Found", e.getMessage());

        String responseJson = response(
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                request.getRequestURI(),
                "The requested resource was not found.",
                e.getMessage(),
                new Date());

        return new ResponseEntity<>(responseJson, NOT_FOUND);
    }

    /**
     * Handles invalid exceptions, returning a 400 response with the appropriate error message.
     *
     * @param e       the exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler({
            InvalidDonationAmountException.class,
            InvalidDonationDateException.class,
            FileException.class,
            PersonException.class,
//            AdoptionFormNotFound.class,
//            UnsupportedOperationException.class
    })
    public ResponseEntity<String> InvalidResourceException(Exception e, HttpServletRequest request) {
        logger.error("{}: {}", "Invalid Request/Resource", e.getMessage());

        String responseJson = response(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                "An invalid resource was requested.",
                e.getMessage(),
                new Date());

        return new ResponseEntity<>(responseJson, BAD_REQUEST);
    }

    /**
     * Handles method argument not valid exceptions, returning a 400 response with the appropriate error message.
     *
     * @param ex      the exception
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String responseJson = response(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                "Validation failed for request.",
                errors.toString(),
                new Date());

        return new ResponseEntity<>(responseJson, BAD_REQUEST);
    }

    /**
     * Handles constraint violation exceptions, returning a 400 response with the appropriate error message.
     *
     * @param ex      the exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        logger.error("Constraint violation: {}", ex.getMessage());

        String responseJson = response(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                "Constraint violation occurred.",
                ex.getMessage(),
                new Date());

        return new ResponseEntity<>(responseJson, BAD_REQUEST);
    }

    /**
     * Handles AI exception, returning a 503 response with the appropriate error message.
     *
     * @param ex      the exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<String> handleAIException(NonTransientAiException ex, HttpServletRequest request) {
        logger.error("AI service is down: {}", ex.getMessage());

        String responseJson = response(
                SERVICE_UNAVAILABLE.value(),
                SERVICE_UNAVAILABLE.getReasonPhrase(),
                request.getRequestURI(),
                "AI service is down, please try again later.",
                "Sorry for the inconvenience.",
                new Date());

        return new ResponseEntity<>(responseJson, SERVICE_UNAVAILABLE);
    }

    /**
     * Handles generic exceptions, returning a 500 response with the appropriate error message.
     *
     * @param e       the exception
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> GenericException(Exception e, HttpServletRequest request) {
        logger.error("{}: {}", "Generic Exception", e.getMessage());

        String responseJson = response(
                INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getRequestURI(),
                "Something went wrong, check the details for more information.",
                e.getMessage(),
                new Date());

        return new ResponseEntity<>(responseJson, INTERNAL_SERVER_ERROR);
    }

    // Wish.com Builder
    // TODO: change this whole thing to a proper builder
    private String response(int status, String error, String path, String message, String details, Date timestamp) {
        return """
                {
                    "status": "%s",
                    "error": "%s",
                    "path": "%s",
                    "message": "%s",
                    "details": "%s",
                    "timestamp": "%s"
                }
                """.formatted(
                status,
                error,
                path,
                message.replaceAll("\"", "'"),
                details,
                timestamp);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(TokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}