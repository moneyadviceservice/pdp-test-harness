package uk.org.ca.stub.simulator.rest.controller;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import uk.org.ca.stub.simulator.rest.api.ServiceAvailabilityApi;
import uk.org.ca.stub.simulator.rest.model.DefaultResponse;
import uk.org.ca.stub.simulator.rest.model.GeneralReportingProperties;
import uk.org.ca.stub.simulator.rest.model.ProblemDetails;
import uk.org.ca.stub.simulator.rest.model.ProblemDetailsErrorsInner;
import uk.org.ca.stub.simulator.rest.model.StubConfiguration;
import uk.org.ca.stub.simulator.service.ConfigurationService;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
@Slf4j
public class ServiceAvailabilityApiController implements ServiceAvailabilityApi {
    
    private static final URI ABOUT_BLANK = URI.create("about:blank");

    private static final String VALUE_NULL_SUBMISSION = "null submission";
    private static final String FIELD_HOLDERNAMEGUID = "holdernameguid";
    private static final String FIELD_UNAVAIL_REASON = "unavail_reason";
    private static final String FIELD_REPORTING_PERIOD_START = "reporting_period_start";
    private static final String FIELD_UNAVAIL_SCHEDULED = "unavail_scheduled";
    private static final String FIELD_RECORD_ID = "record_id";
    private static final String FIELD_SUBMISSION_STATUS = "submission_status";
    private static final String FIELD_SUBMISSION_ID = "submission_id";
    private static final String FIELD_SUBMISSION_REASON = "submission_reason";
    private static final String FIELD_ITEMS = "items";
    private static final String FIELD_START_DATE_TS = "start_date_ts";
    private static final String FIELD_END_DATE_TS = "end_date_ts";
    private static final String FIELD_UNEXPECTED = "unexpectedField";

    private final ConfigurationService configurationService;
    private final Validator validator;
    private final ObjectMapper objectMapper;
    
    public ServiceAvailabilityApiController(ConfigurationService configurationService, Validator validator, ObjectMapper objectMapper) {
        this.configurationService = configurationService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }
    
    private static final java.util.Set<String> ALLOWED_SERVICE_AVAILABILITY_FIELDS = java.util.Set.of(
            FIELD_RECORD_ID, FIELD_SUBMISSION_STATUS, FIELD_REPORTING_PERIOD_START,
            FIELD_SUBMISSION_ID, FIELD_SUBMISSION_REASON, FIELD_ITEMS
    );

    private <T> T convertToType(Object obj, Class<T> targetClass) {
        return objectMapper.convertValue(obj, targetClass);
    }

    private static final java.util.Set<String> ALLOWED_UNAVAILABILITY_ITEM_FIELDS = java.util.Set.of(
            FIELD_UNAVAIL_SCHEDULED, FIELD_UNAVAIL_REASON, FIELD_START_DATE_TS, FIELD_END_DATE_TS
    );

    private ResponseEntity<DefaultResponse> checkUnexpectedFields(Object rawRequest) {
        if (!(rawRequest instanceof java.util.Map<?, ?> map)) return null;
        for (Object key : map.keySet()) {
            if (key instanceof String s && !ALLOWED_SERVICE_AVAILABILITY_FIELDS.contains(s)) {
                log.warn("Unexpected top-level field in request: {}", key);
                return createManualValidationError(FIELD_UNEXPECTED);
            }
        }
        return checkUnexpectedNestedItemFields(map.get(FIELD_ITEMS));
    }

    private ResponseEntity<DefaultResponse> checkUnexpectedNestedItemFields(Object items) {
        if (!(items instanceof java.util.List<?> itemList)) return null;
        for (Object holderMap : itemList) {
            if (holderMap instanceof java.util.Map<?, ?> holders) {
                ResponseEntity<DefaultResponse> err = checkUnexpectedPeriodFields(holders);
                if (err != null) return err;
            }
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkUnexpectedPeriodFields(java.util.Map<?, ?> holders) {
        for (Object periods : holders.values()) {
            if (!(periods instanceof java.util.List<?> periodList)) continue;
            ResponseEntity<DefaultResponse> err = checkUnexpectedFieldsInPeriodList(periodList);
            if (err != null) return err;
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkUnexpectedFieldsInPeriodList(java.util.List<?> periodList) {
        for (Object period : periodList) {
            if (!(period instanceof java.util.Map<?, ?> periodMap)) continue;
            for (Object field : periodMap.keySet()) {
                if (field instanceof String s && !ALLOWED_UNAVAILABILITY_ITEM_FIELDS.contains(s)) {
                    log.warn("Unexpected field in unavailability item: {}", field);
                    return createManualValidationError(FIELD_UNEXPECTED);
                }
            }
        }
        return null;
    }

    private static final java.util.Set<String> REQUIRED_SERVICE_AVAILABILITY_FIELDS = java.util.Set.of(
            FIELD_RECORD_ID, FIELD_SUBMISSION_STATUS, FIELD_REPORTING_PERIOD_START, FIELD_SUBMISSION_REASON, FIELD_SUBMISSION_ID, FIELD_ITEMS
    );

    private static final java.util.Set<String> REQUIRED_UNAVAILABILITY_ITEM_FIELDS = java.util.Set.of(
            FIELD_UNAVAIL_SCHEDULED, FIELD_UNAVAIL_REASON, FIELD_START_DATE_TS, FIELD_END_DATE_TS
    );

    private static final java.util.Set<String> VALID_SUBMISSION_REASONS = java.util.Set.of(
            "initial submission", "resubmission", VALUE_NULL_SUBMISSION
    );

    private static final java.util.Set<String> VALID_SUBMISSION_STATUSES = java.util.Set.of(
            "on time", "correction", "late"
    );

    private static final java.util.regex.Pattern ISO_DATETIME_PATTERN =
            java.util.regex.Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})$");

    private static final java.util.regex.Pattern UNAVAIL_REASON_PATTERN =
            java.util.regex.Pattern.compile("^[a-zA-Z0-9_ .-]*$");

    private ResponseEntity<DefaultResponse> checkFieldFormats(Object rawRequest) {
        if (!(rawRequest instanceof java.util.Map<?, ?> map)) return null;
        ResponseEntity<DefaultResponse> err = checkUuidFields(map);
        if (err != null) return err;
        Object submissionReason = map.get(FIELD_SUBMISSION_REASON);
        if (submissionReason != null && (!(submissionReason instanceof String s) || !VALID_SUBMISSION_REASONS.contains(s))) {
            return createManualValidationError("submissionReason");
        }
        Object submissionStatus = map.get(FIELD_SUBMISSION_STATUS);
        if (submissionStatus != null && (!(submissionStatus instanceof String s) || !VALID_SUBMISSION_STATUSES.contains(s))) {
            return createManualValidationError("submissionStatus");
        }
        Object reportingPeriodStart = map.get(FIELD_REPORTING_PERIOD_START);
        if (reportingPeriodStart != null && (!(reportingPeriodStart instanceof String s) || !ISO_DATETIME_PATTERN.matcher(s).matches())) {
            return createManualValidationError("reportingPeriodStart");
        }
        boolean isNullSubmission = VALUE_NULL_SUBMISSION.equals(submissionReason);
        return checkItemsFieldFormats(map.get(FIELD_ITEMS), isNullSubmission);
    }

    private ResponseEntity<DefaultResponse> checkUuidFields(java.util.Map<?, ?> map) {
        Object recordId = map.get(FIELD_RECORD_ID);
        if (recordId != null && (!(recordId instanceof String s) || !isValidUuid(s))) {
            return createManualValidationError("recordId");
        }
        Object submissionId = map.get(FIELD_SUBMISSION_ID);
        if (submissionId != null && (!(submissionId instanceof String s) || !isValidUuid(s))) {
            return createManualValidationError("submissionId");
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkItemsFieldFormats(Object items, boolean isNullSubmission) {
        if (items == null || isNullSubmission) return null;
        if (!(items instanceof java.util.List<?> list) || list.isEmpty()) {
            return createManualValidationError(FIELD_HOLDERNAMEGUID);
        }
        for (Object holderMap : list) {
            if (!(holderMap instanceof java.util.Map<?, ?> holders)) {
                return createManualValidationError(FIELD_HOLDERNAMEGUID);
            }
            ResponseEntity<DefaultResponse> err = checkHolderPeriodsFieldFormats(holders);
            if (err != null) return err;
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkHolderPeriodsFieldFormats(java.util.Map<?, ?> holders) {
        for (java.util.Map.Entry<?, ?> entry : holders.entrySet()) {
            if (!(entry.getValue() instanceof java.util.List<?> periodList)) {
                return createManualValidationError(FIELD_HOLDERNAMEGUID);
            }
            for (Object period : periodList) {
                if (period instanceof java.util.Map<?, ?> periodMap) {
                    ResponseEntity<DefaultResponse> err = checkPeriodFieldFormats(periodMap);
                    if (err != null) return err;
                }
            }
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkPeriodFieldFormats(java.util.Map<?, ?> periodMap) {
        Object unavailScheduled = periodMap.get(FIELD_UNAVAIL_SCHEDULED);
        if (unavailScheduled != null && !(unavailScheduled instanceof Boolean)) {
            return createManualValidationError("unavailScheduled");
        }
        Object unavailReason = periodMap.get(FIELD_UNAVAIL_REASON);
        if (unavailReason != null && (!(unavailReason instanceof String s) || !UNAVAIL_REASON_PATTERN.matcher(s).matches())) {
            return createManualValidationError("unavailReason");
        }
        Object startDateTs = periodMap.get(FIELD_START_DATE_TS);
        if (startDateTs != null && (!(startDateTs instanceof String s) || !ISO_DATETIME_PATTERN.matcher(s).matches())) {
            return createManualValidationError("startDateTs");
        }
        Object endDateTs = periodMap.get(FIELD_END_DATE_TS);
        if (endDateTs != null && (!(endDateTs instanceof String s) || !ISO_DATETIME_PATTERN.matcher(s).matches())) {
            return createManualValidationError("endDateTs");
        }
        return null;
    }

    private static boolean isValidUuid(String s) {
        try {
            UUID.fromString(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private ResponseEntity<DefaultResponse> checkRequiredTopLevelFields(Object rawRequest) {
        if (!(rawRequest instanceof java.util.Map<?, ?> map)) return null;
        boolean isNullSubmission = VALUE_NULL_SUBMISSION.equals(map.get(FIELD_SUBMISSION_REASON));
        for (String required : REQUIRED_SERVICE_AVAILABILITY_FIELDS) {
            boolean missing = !map.containsKey(required);
            boolean nullValue = !missing && map.get(required) == null;
            if (nullValue && isNullSubmission && FIELD_SUBMISSION_ID.equals(required)) continue;
            if (missing || nullValue) {
                return missingTopLevelFieldError(required);
            }
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> missingTopLevelFieldError(String required) {
        log.warn("Missing required field in request: {}", required);
        if (FIELD_ITEMS.equals(required)) {
            return createManualValidationError(FIELD_HOLDERNAMEGUID);
        }
        String camel = java.util.Arrays.stream(required.split("_"))
                .reduce("", (acc, part) -> acc.isEmpty()
                        ? part
                        : acc + part.substring(0, 1).toUpperCase() + part.substring(1));
        return createManualValidationError(camel);
    }

    private ResponseEntity<DefaultResponse> checkRequiredItemFields(Object rawRequest) {
        if (!(rawRequest instanceof java.util.Map<?, ?> map)) return null;
        Object items = map.get(FIELD_ITEMS);
        if (!(items instanceof java.util.List<?> itemList)) return null;
        for (Object holderMap : itemList) {
            if (holderMap instanceof java.util.Map<?, ?> holders) {
                ResponseEntity<DefaultResponse> err = checkRequiredPeriodFields(holders);
                if (err != null) return err;
            }
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkRequiredPeriodFields(java.util.Map<?, ?> holders) {
        for (Object periods : holders.values()) {
            if (!(periods instanceof java.util.List<?> periodList)) continue;
            for (Object period : periodList) {
                ResponseEntity<DefaultResponse> err = checkRequiredPeriodFieldPresence(period);
                if (err != null) return err;
            }
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkRequiredPeriodFieldPresence(Object period) {
        if (!(period instanceof java.util.Map<?, ?> periodMap)) return null;
        for (String required : REQUIRED_UNAVAILABILITY_ITEM_FIELDS) {
            if (!periodMap.containsKey(required) || periodMap.get(required) == null) {
                log.warn("Missing required field in unavailability period: {}", required);
                String camel = java.util.Arrays.stream(required.split("_"))
                        .reduce("", (acc, part) -> acc.isEmpty()
                                ? part
                                : acc + part.substring(0, 1).toUpperCase() + part.substring(1));
                return createManualValidationError(camel);
            }
        }
        return null;
    }

    private <T> ResponseEntity<DefaultResponse> validateAndProcess(T requestBody, String endpoint) {
        // Validate the request body
        Set<ConstraintViolation<T>> violations;
        try {
            violations = validator.validate(requestBody);
        } catch (jakarta.validation.UnexpectedTypeException e) {
            // Skip validation errors for enum fields with @Size annotations (OpenAPI generator bug)
            log.warn("Validation error (likely @Size on enum): {}", e.getMessage());
            violations = java.util.Collections.emptySet();
        }
        
        if (!violations.isEmpty()) {
            log.warn("Validation failed for {}: {}", endpoint, violations);
            return createValidationErrorResponse(violations);
        }
        
        // Check for stubbed error configuration
        StubConfiguration config = configurationService.getAndDecrementConfiguration(endpoint);
        if (config != null) {
            log.info("Returning stubbed error response for {} with status {} and code {}", 
                    endpoint, config.getStatus(), config.getCode());
            return createErrorResponse(config);
        }
        
        // Normal response
        DefaultResponse response = new DefaultResponse();
        response.setMessage("ACCEPTED");
        response.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    
    private <T> ResponseEntity<DefaultResponse> createValidationErrorResponse(Set<ConstraintViolation<T>> violations) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Bad Request");
        problemDetails.setStatus(400);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        
        List<ProblemDetailsErrorsInner> errors = new ArrayList<>();
        for (ConstraintViolation<T> violation : violations) {
            ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
            // Combine property path and message in the code field
            String errorCode = violation.getPropertyPath().toString().toUpperCase().replace(".", "_") + "_" + violation.getMessage().toUpperCase().replace(" ", "_");
            error.setCode(errorCode);
            errors.add(error);
        }
        problemDetails.setErrors(errors);
        
        @SuppressWarnings("unchecked")
        ResponseEntity<DefaultResponse> response = (ResponseEntity<DefaultResponse>)(ResponseEntity<?>) new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
        return response;
    }
    
    private ResponseEntity<DefaultResponse> checkPayloadSize() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            long contentLength = attrs.getRequest().getContentLengthLong();
            if (contentLength > 1_048_576L) {
                log.warn("Payload too large: {} bytes", contentLength);
                ProblemDetails problemDetails = new ProblemDetails();
                problemDetails.setType(ABOUT_BLANK);
                problemDetails.setTitle("Payload Too Large");
                problemDetails.setStatus(413);
                problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
                ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
                error.setCode("CONTENT_TOO_LARGE");
                problemDetails.setErrors(java.util.List.of(error));
                @SuppressWarnings("unchecked")
                ResponseEntity<DefaultResponse> response = (ResponseEntity<DefaultResponse>)(ResponseEntity<?>) new ResponseEntity<>(problemDetails, HttpStatus.PAYLOAD_TOO_LARGE);
                return response;
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<DefaultResponse> findServiceUnavailabilityData(
            UUID xRequestID,
            Object findServiceUnavailabilityDataRequest) {
        
        log.info("Received find service unavailability data request with X-Request-ID: {}", xRequestID);
        ResponseEntity<DefaultResponse> sizeError = checkPayloadSize();
        if (sizeError != null) return sizeError;
        if (findServiceUnavailabilityDataRequest instanceof java.util.Map<?, ?> m && m.isEmpty()) {
            return createManualValidationError("requestBody");
        }
        ResponseEntity<DefaultResponse> ruleError = checkNullSubmissionRule(findServiceUnavailabilityDataRequest);
        if (ruleError != null) return ruleError;
        ResponseEntity<DefaultResponse> requiredError = checkRequiredTopLevelFields(findServiceUnavailabilityDataRequest);
        if (requiredError != null) return requiredError;
        ResponseEntity<DefaultResponse> formatError = checkFieldFormats(findServiceUnavailabilityDataRequest);
        if (formatError != null) return formatError;
        ResponseEntity<DefaultResponse> fieldError = checkUnexpectedFields(findServiceUnavailabilityDataRequest);
        if (fieldError != null) return fieldError;
        ResponseEntity<DefaultResponse> itemFieldError = checkRequiredItemFields(findServiceUnavailabilityDataRequest);
        if (itemFieldError != null) return itemFieldError;
        try {
            GeneralReportingProperties request = convertToType(findServiceUnavailabilityDataRequest, GeneralReportingProperties.class);
            return validateAndProcess(request, "/service-availability/find");
        } catch (IllegalArgumentException e) {
            log.warn("Type conversion failed for /service-availability/find: {}", e.getMessage());
            return createManualValidationError("request");
        }
    }
    
    @Override
    public ResponseEntity<DefaultResponse> viewServiceUnavailabilityData(
            UUID xRequestID,
            Object findServiceUnavailabilityDataRequest) {
        
        log.info("Received view service unavailability data request with X-Request-ID: {}", xRequestID);
        ResponseEntity<DefaultResponse> sizeError = checkPayloadSize();
        if (sizeError != null) return sizeError;
        if (findServiceUnavailabilityDataRequest instanceof java.util.Map<?, ?> m && m.isEmpty()) {
            return createManualValidationError("requestBody");
        }
        ResponseEntity<DefaultResponse> ruleError = checkNullSubmissionRule(findServiceUnavailabilityDataRequest);
        if (ruleError != null) return ruleError;
        ResponseEntity<DefaultResponse> requiredError = checkRequiredTopLevelFields(findServiceUnavailabilityDataRequest);
        if (requiredError != null) return requiredError;
        ResponseEntity<DefaultResponse> formatError = checkFieldFormats(findServiceUnavailabilityDataRequest);
        if (formatError != null) return formatError;
        ResponseEntity<DefaultResponse> fieldError = checkUnexpectedFields(findServiceUnavailabilityDataRequest);
        if (fieldError != null) return fieldError;
        ResponseEntity<DefaultResponse> itemFieldError = checkRequiredItemFields(findServiceUnavailabilityDataRequest);
        if (itemFieldError != null) return itemFieldError;
        try {
            GeneralReportingProperties request = convertToType(findServiceUnavailabilityDataRequest, GeneralReportingProperties.class);
            return validateAndProcess(request, "/service-availability/view");
        } catch (IllegalArgumentException e) {
            log.warn("Type conversion failed for /service-availability/view: {}", e.getMessage());
            return createManualValidationError("request");
        }
    }
    
    private ResponseEntity<DefaultResponse> createManualValidationError(String fieldName) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Bad Request");
        problemDetails.setStatus(400);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));

        String fieldNameUpper = fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        String errorCode = "INVALID_" + fieldNameUpper;

        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        error.setCode(errorCode);
        problemDetails.setErrors(List.of(error));

        @SuppressWarnings("unchecked")
        ResponseEntity<DefaultResponse> response = (ResponseEntity<DefaultResponse>) (ResponseEntity<?>)
                new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
        return response;
    }

    private ResponseEntity<DefaultResponse> checkNullSubmissionRule(Object rawRequest) {
        if (rawRequest instanceof java.util.Map<?, ?> map) {
            Object reason = map.get(FIELD_SUBMISSION_REASON);
            if (VALUE_NULL_SUBMISSION.equals(reason)) {
                // submission_id must be null
                Object submissionId = map.get(FIELD_SUBMISSION_ID);
                if (submissionId != null) {
                    log.warn("Null submission request must have null submission_id, but got: {}", submissionId);
                    return createManualValidationError("submissionId");
                }
                // items must be an array of UUID strings, not objects with data
                Object items = map.get(FIELD_ITEMS);
                if (items instanceof java.util.List<?> list && !list.isEmpty()
                        && !(list.get(0) instanceof String)) {
                    log.warn("Null submission items must be UUID strings, but got objects");
                    return createManualValidationError(FIELD_UNEXPECTED);
                }
            }
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> createErrorResponse(StubConfiguration config) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(URI.create("https://example.com/errors/" + config.getCode()));
        problemDetails.setTitle(toTitle(config.getCode()));
        problemDetails.setStatus(config.getStatus());
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        
        // Convert stub configuration errors to problem details errors
        List<ProblemDetailsErrorsInner> errors = config.getErrors().stream()
                .map(error -> {
                    ProblemDetailsErrorsInner problemError = new ProblemDetailsErrorsInner();
                    problemError.setCode(error.getCode());
                    return problemError;
                })
                .toList();
        
        problemDetails.setErrors(errors);
        
        // Cast to DefaultResponse (this is a workaround for the API signature)
        // In reality, the response will be serialized as ProblemDetails
        @SuppressWarnings("unchecked")
        ResponseEntity<DefaultResponse> response = (ResponseEntity<DefaultResponse>)(ResponseEntity<?>) new ResponseEntity<>(problemDetails, HttpStatus.valueOf(config.getStatus()));
        return response;
    }

    private static String toTitle(String code) {
        if (code == null || code.isBlank()) return code;
        return Arrays.stream(code.split("_"))
                .map(w -> w.charAt(0) + w.substring(1).toLowerCase())
                .collect(java.util.stream.Collectors.joining(" "));
    }
}
