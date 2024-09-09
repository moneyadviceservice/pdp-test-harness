package uk.org.ca.stub.simulator.rest.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler {
    private JsonMapper jsonMapper = new JsonMapper();
    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    private void logHandledError(Exception ex, String url){
        logger.debug("{} for request {} :  {} ", ex.getClass().getName(), url, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElement(NotFoundException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        return ex.getMessage();
    }

    // UNAUTHORIZED errors usually do not have bodies, but we put one just for testing purposes
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", ex.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(InvalidRequestException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        return ex.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> constraintViolationError(ConstraintViolationException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        return ex.getConstraintViolations().stream().map(constraintViolation ->  {
            try {
                return jsonMapper.writeValueAsString(new CasValidationError(constraintViolation.getPropertyPath().toString(), constraintViolation.getInvalidValue(), constraintViolation.getMessage()));
            } catch (JsonProcessingException exc) {
                logger.debug("could not write validation error body", exc);
                return null;
            }
        }).toList();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> validationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        return ex.getBindingResult().getFieldErrors().stream()
                .map(e -> {
                    try {
                        return jsonMapper.writeValueAsString(new CasValidationError(e.getField(), e.getRejectedValue(), e.getDefaultMessage()));
                    } catch (JsonProcessingException exc) {
                        logger.debug("could not write validation error body", exc);
                        return null;
                    }
                })
                .toList();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(ConflictException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        return ex.getMessage();
    }
}
