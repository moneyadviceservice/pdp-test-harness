package uk.org.ca.stub.simulator.rest.exception;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import uk.org.ca.stub.simulator.rest.model.ProblemDetails;
import uk.org.ca.stub.simulator.rest.model.ProblemDetailsErrorsInner;

@RestControllerAdvice
public class GeneralExceptionHandler {
    private JsonMapper jsonMapper = new JsonMapper();
    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
    private static final URI ABOUT_BLANK = URI.create("about:blank");

    private void logHandledError(Exception ex, String url){
        logger.debug("{} for request {} :  {} ", ex.getClass().getName(), url, ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetails> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Method Not Allowed");
        problemDetails.setStatus(405);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        error.setCode("METHOD_NOT_ALLOWED");
        problemDetails.setErrors(List.of(error));
        return new ResponseEntity<>(problemDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetails> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Bad Request");
        problemDetails.setStatus(400);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        // Normalise to UPPER_SNAKE_CASE: handle both "xRequestID" (camelCase) and "X-Request-ID" (header name)
        String paramName = ex.getName();
        String upperSnake = paramName
                .replace("-", "_")                              // X-Request-ID -> X_Request_ID
                .replaceAll("([a-z])([A-Z])", "$1_$2")         // camelCase -> snake_case
                .toUpperCase();                                  // -> UPPER_SNAKE_CASE
        error.setCode("INVALID_" + upperSnake);
        problemDetails.setErrors(List.of(error));
        return new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetails> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Bad Request");
        problemDetails.setStatus(400);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        error.setCode("INVALID_REQUEST_BODY");
        problemDetails.setErrors(List.of(error));
        return new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ProblemDetails> handlePayloadTooLarge(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        logHandledError(ex, request.getRequestURI());
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Payload Too Large");
        problemDetails.setStatus(413);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        error.setCode("CONTENT_TOO_LARGE");
        problemDetails.setErrors(List.of(error));
        return new ResponseEntity<>(problemDetails, HttpStatus.PAYLOAD_TOO_LARGE);
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
