package uk.org.ca.stub.simulator.rest.controller;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import uk.org.ca.stub.simulator.rest.api.ViewResponseApi;
import uk.org.ca.stub.simulator.rest.model.CalculationRequest;
import uk.org.ca.stub.simulator.rest.model.DefaultResponse;
import uk.org.ca.stub.simulator.rest.model.ProblemDetails;
import uk.org.ca.stub.simulator.rest.model.ProblemDetailsErrorsInner;
import uk.org.ca.stub.simulator.rest.model.StubConfiguration;
import uk.org.ca.stub.simulator.rest.model.ViewResponseRequestNumberData;
import uk.org.ca.stub.simulator.rest.model.ViewResponseResponseTimesData;
import uk.org.ca.stub.simulator.service.ConfigurationService;

@RestController
@RequestMapping("${openapi.mergedSpec.base-path:}")
@Slf4j
public class ViewResponseApiController implements ViewResponseApi {
    
    private static final java.net.URI ABOUT_BLANK = java.net.URI.create("about:blank");

    private final ConfigurationService configurationService;
    private final Validator validator;
    private final ObjectMapper objectMapper;
    
    public ViewResponseApiController(ConfigurationService configurationService, Validator validator, ObjectMapper objectMapper) {
        this.configurationService = configurationService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }
    
    private <T> T convertToType(Object obj, Class<T> targetClass) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(obj);
            return objectMapper.readerFor(targetClass)
                    .with(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(json);
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private boolean isEmptyBody(Object obj) {
        return obj instanceof Map<?, ?> map && map.isEmpty();
    }

    private String extractFieldNameFromException(IllegalArgumentException e) {
        if (e.getCause() instanceof UnrecognizedPropertyException) {
            return FIELD_UNEXPECTED;
        }
        if (e.getCause() instanceof JsonMappingException jme && !jme.getPath().isEmpty()) {
            return jme.getPath().getLast().getFieldName();
        }
        return "request";
    }
    
    private <T> ResponseEntity<DefaultResponse> validateAndProcess(T requestBody, String endpoint) {
        // Log the actual type for debugging
        log.info("Request body type for {}: {}", endpoint, requestBody.getClass().getName());
        
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
        response.setMessage(MSG_ACCEPTED);
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
    
    private ResponseEntity<DefaultResponse> createManualValidationError(String fieldName, String message) {
        log.warn("Validation error on field '{}': {}", fieldName, message);
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Bad Request");
        problemDetails.setStatus(400);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        
        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        // Convert camelCase to UPPER_SNAKE_CASE with spec exceptions
        String fieldNameUpper;
        if (FIELD_HOLDERNAME_GUID.equals(fieldName)) {
            fieldNameUpper = "HOLDERNAMEGUID";
        } else {
            fieldNameUpper = fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        }
        String errorCode = "INVALID_" + fieldNameUpper;
        error.setCode(errorCode);
        
        List<ProblemDetailsErrorsInner> errors = new ArrayList<>();
        errors.add(error);
        problemDetails.setErrors(errors);
        
        @SuppressWarnings("unchecked")
        ResponseEntity<DefaultResponse> response = (ResponseEntity<DefaultResponse>)(ResponseEntity<?>) new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
        return response;
    }
    
    // ---- Constants for calculations validation ----
    private static final String VALUE_NULL_SUBMISSION = "null submission";
    private static final java.util.Set<String> VALID_CALCULATION_SUBMISSION_REASONS = java.util.Set.of(
        "initial submission", "resubmission", VALUE_NULL_SUBMISSION
    );
    private static final java.util.Set<String> VALID_CALCULATION_SUBMISSION_STATUSES = java.util.Set.of(
        "on time", "correction", "late"
    );
    private static final java.util.Set<String> VALID_VALUE_CODES = java.util.Set.of("DBC", "DCC");
    private static final String FIELD_RECORD_ID = "record_id";
    private static final String FIELD_HOLDERNAME_GUID = "holdernameGuid";
    private static final String FIELD_SUBMISSION_ID = "submission_id";
    private static final String FIELD_SUBMISSION_REASON = "submission_reason";
    private static final String FIELD_SUBMISSION_STATUS = "submission_status";
    private static final String FIELD_REPORTING_PERIOD_START = "reporting_period_start";
    private static final String MSG_MUST_NOT_BE_NULL = "must not be null";
    private static final String MSG_MUST_NOT_BE_EMPTY = "must not be empty";
    private static final String MSG_INVALID_UUID = "invalid UUID";
    private static final String MSG_INVALID_VALUE = "invalid value";
    private static final String MSG_INVALID_FORMAT = "invalid format";
    private static final String FIELD_SUBMISSION_STATUS_CAMEL = "submissionStatus";
    private static final String FIELD_SUBMISSION_ID_CAMEL = "submissionId";
    private static final String FIELD_SUBMISSION_REASON_CAMEL = "submissionReason";
    private static final String FIELD_REQ_DATE_TS_CAMEL = "reqDateTs";
    private static final String FIELD_RESP_DATE_TS_CAMEL = "respDateTs";
    private static final String FIELD_UNEXPECTED = "unexpectedField";
    private static final String FIELD_CALCULATION_TYPE = "calculation_type";
    private static final String FIELD_VALUE_CODE = "value_code";
    private static final String FIELD_CALC_START_DATE = "calculation_start_date";
    private static final String FIELD_CALC_END_DATE = "calculation_end_date";
    private static final String FIELD_REQ_DATE_TS = "req_date_ts";
    private static final String FIELD_RESP_DATE_TS = "resp_date_ts";
    private static final String FIELD_ITEMS = "items";
    private static final String FIELD_REQUEST_BODY = "requestBody";
    private static final String FIELD_CALCULATION_TYPE_CAMEL = "calculationType";
    private static final String FIELD_RECORD_ID_CAMEL = "recordId";
    private static final String FIELD_REPORTING_PERIOD_START_CAMEL = "reportingPeriodStart";
    private static final String FIELD_VALUE_CODE_CAMEL = "valueCode";
    private static final String MSG_ACCEPTED = "ACCEPTED";
    private static final String MSG_UNEXPECTED_FIELD_PREFIX = "unexpected field: ";
    private static final java.util.Set<String> ALLOWED_UNAVAIL_FIELDS = java.util.Set.of(
        FIELD_HOLDERNAME_GUID, FIELD_RECORD_ID, FIELD_SUBMISSION_ID, FIELD_SUBMISSION_REASON,
        FIELD_SUBMISSION_STATUS, FIELD_REPORTING_PERIOD_START,
        "contact_count", "missingadmin_count", "temperror_count",
        "eri_unavail_ano_count", "eri_unavail_ppf_count", "eri_unavail_trn_count",
        "accrued_unavail_ano_count", "accrued_unavail_ppf_count", "accrued_unavail_trn_count"
    );
    private static final java.util.regex.Pattern CALC_ISO_DATETIME_PATTERN =
        java.util.regex.Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})$");
    private static final java.util.Set<String> ALLOWED_CALC_ITEM_FIELDS = java.util.Set.of(
        FIELD_VALUE_CODE, FIELD_CALC_START_DATE, FIELD_CALC_END_DATE
    );
    private static final java.util.Set<String> ALLOWED_TIME_ITEM_FIELDS = java.util.Set.of(
        FIELD_REQ_DATE_TS, FIELD_RESP_DATE_TS
    );

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<DefaultResponse> viewResponseCalculation(
            UUID xRequestID,
            Object viewResponseCalculationRequest) {

        log.info("Received view response calculation request with X-Request-ID: {}", xRequestID);
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getContentLengthLong() > 1_048_576L) {
            return createPayloadTooLargeResponse();
        }
        if (!(viewResponseCalculationRequest instanceof Map)) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }
        Map<String, Object> m = (Map<String, Object>) viewResponseCalculationRequest;
        if (m.isEmpty()) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }

        Object submissionReasonRaw = m.get(FIELD_SUBMISSION_REASON);
        boolean isNullSubmission = VALUE_NULL_SUBMISSION.equals(submissionReasonRaw);

        ResponseEntity<DefaultResponse> err;
        if ((err = validateCalcNullSubmissionRules(m, isNullSubmission)) != null) return err;
        if ((err = validateCommonRequiredFields(m, isNullSubmission)) != null) return err;
        if (!isNullSubmission && (!m.containsKey(FIELD_CALCULATION_TYPE) || m.get(FIELD_CALCULATION_TYPE) == null)) {
            return createManualValidationError(FIELD_CALCULATION_TYPE_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if ((err = validateCommonFormats(m, submissionReasonRaw)) != null) return err;

        Object calcTypeRaw = m.get(FIELD_CALCULATION_TYPE);
        if (calcTypeRaw != null && !calcTypeRaw.toString().equals("single") && !calcTypeRaw.toString().equals("multiple")) {
            return createManualValidationError(FIELD_CALCULATION_TYPE_CAMEL, MSG_INVALID_VALUE);
        }
        if ((err = validateCalcTypeData(m, isNullSubmission, calcTypeRaw)) != null) return err;

        CalculationRequest request;
        try {
            request = convertToType(viewResponseCalculationRequest, CalculationRequest.class);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid data format in calculations: {}", e.getMessage());
            return createManualValidationError(extractFieldNameFromException(e), MSG_INVALID_VALUE);
        }
        return validateAndProcess(request, "/view-response/calculations");
    }

    private ResponseEntity<DefaultResponse> validateCalcNullSubmissionRules(
            Map<String, Object> m, boolean isNullSubmission) {
        if (!isNullSubmission) return null;
        if (m.get(FIELD_SUBMISSION_ID) != null) {
            return createManualValidationError(FIELD_SUBMISSION_ID_CAMEL, "must be null for null submission");
        }
        if (m.containsKey("single_calculation") || m.containsKey("multiple_calculation")) {
            return createManualValidationError(FIELD_UNEXPECTED, "null submission must not have calculation data");
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> validateCommonRequiredFields(
            Map<String, Object> m, boolean isNullSubmission) {
        if (!m.containsKey(FIELD_HOLDERNAME_GUID) || m.get(FIELD_HOLDERNAME_GUID) == null) {
            return createManualValidationError(FIELD_HOLDERNAME_GUID, MSG_MUST_NOT_BE_NULL);
        }
        if (!m.containsKey(FIELD_RECORD_ID) || m.get(FIELD_RECORD_ID) == null) {
            return createManualValidationError(FIELD_RECORD_ID_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (!m.containsKey(FIELD_SUBMISSION_REASON) || m.get(FIELD_SUBMISSION_REASON) == null) {
            return createManualValidationError(FIELD_SUBMISSION_REASON_CAMEL, MSG_MUST_NOT_BE_NULL);
        }

        if (!m.containsKey(FIELD_SUBMISSION_STATUS) || m.get(FIELD_SUBMISSION_STATUS) == null) {
            return createManualValidationError(FIELD_SUBMISSION_STATUS_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (!m.containsKey(FIELD_REPORTING_PERIOD_START) || m.get(FIELD_REPORTING_PERIOD_START) == null) {
            return createManualValidationError(FIELD_REPORTING_PERIOD_START_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (!isNullSubmission && !m.containsKey(FIELD_SUBMISSION_ID)) {
            return createManualValidationError(FIELD_SUBMISSION_ID_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> validateCommonFormats(
            Map<String, Object> m, Object submissionReasonRaw) {
        ResponseEntity<DefaultResponse> err;
        if ((err = validateUuidFormats(m)) != null) return err;
        if (submissionReasonRaw != null && (!(submissionReasonRaw instanceof String sr) || !VALID_CALCULATION_SUBMISSION_REASONS.contains(sr))) {
            return createManualValidationError(FIELD_SUBMISSION_REASON_CAMEL, MSG_INVALID_VALUE);
        }
        Object submissionIdRaw = m.get(FIELD_SUBMISSION_ID);
        if (submissionIdRaw != null) {
            if (!(submissionIdRaw instanceof String)) {
                return createManualValidationError(FIELD_SUBMISSION_ID_CAMEL, MSG_INVALID_UUID);
            }
            try { java.util.UUID.fromString(submissionIdRaw.toString()); }
            catch (IllegalArgumentException e) { return createManualValidationError(FIELD_SUBMISSION_ID_CAMEL, MSG_INVALID_UUID); }
        }
        Object submissionStatusRaw = m.get(FIELD_SUBMISSION_STATUS);
        if (submissionStatusRaw != null && (!(submissionStatusRaw instanceof String ss) || !VALID_CALCULATION_SUBMISSION_STATUSES.contains(ss))) {
            return createManualValidationError(FIELD_SUBMISSION_STATUS_CAMEL, MSG_INVALID_VALUE);
        }
        Object reportingPeriodStartRaw = m.get(FIELD_REPORTING_PERIOD_START);
        if (reportingPeriodStartRaw != null && !CALC_ISO_DATETIME_PATTERN.matcher(reportingPeriodStartRaw.toString()).matches()) {
            return createManualValidationError(FIELD_REPORTING_PERIOD_START_CAMEL, MSG_INVALID_FORMAT);
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> validateUuidFormats(Map<String, Object> m) {
        Object holdernameGuidRaw = m.get(FIELD_HOLDERNAME_GUID);
        if (holdernameGuidRaw != null) {
            try { java.util.UUID.fromString(holdernameGuidRaw.toString()); }
            catch (IllegalArgumentException e) { return createManualValidationError(FIELD_HOLDERNAME_GUID, MSG_INVALID_UUID); }
        }
        Object recordIdRaw = m.get(FIELD_RECORD_ID);
        if (recordIdRaw != null) {
            try { java.util.UUID.fromString(recordIdRaw.toString()); }
            catch (IllegalArgumentException e) { return createManualValidationError(FIELD_RECORD_ID_CAMEL, MSG_INVALID_UUID); }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateCalcTypeData(
            Map<String, Object> m, boolean isNullSubmission, Object calcTypeRaw) {
        if (isNullSubmission || calcTypeRaw == null) return null;
        String calcType = calcTypeRaw.toString();
        if (calcType.equals("single")) {
            return validateSingleCalcData(m);
        } else if (calcType.equals("multiple")) {
            return validateMultipleCalcData(m);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateSingleCalcData(Map<String, Object> m) {
        Object singleCalcRaw = m.get("single_calculation");
        if (singleCalcRaw == null) {
            return createManualValidationError(FIELD_CALCULATION_TYPE_CAMEL, "single_calculation required");
        }
        if (singleCalcRaw instanceof Map) {
            return validateCalcDataArray((Map<String, Object>) singleCalcRaw);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateMultipleCalcData(Map<String, Object> m) {
        Object multipleCalcRaw = m.get("multiple_calculation");
        if (multipleCalcRaw == null) {
            return createManualValidationError(FIELD_CALCULATION_TYPE_CAMEL, "multiple_calculation required");
        }
        if (!(multipleCalcRaw instanceof Map)) return null;
        Map<String, Object> multipleCalc = (Map<String, Object>) multipleCalcRaw;
        Object eriDataRaw = multipleCalc.get("eriCalculationData");
        if (eriDataRaw == null) {
            return createManualValidationError(FIELD_CALCULATION_TYPE_CAMEL, "eriCalculationData required");
        }
        if (eriDataRaw instanceof Map) {
            ResponseEntity<DefaultResponse> err = validateCalcDataArray((Map<String, Object>) eriDataRaw);
            if (err != null) return err;
        }
        Object accruedDataRaw = multipleCalc.get("accruedCalculationData");
        if (accruedDataRaw == null) {
            return createManualValidationError(FIELD_CALCULATION_TYPE_CAMEL, "accruedCalculationData required");
        }
        if (accruedDataRaw instanceof Map) {
            return validateCalcDataArray((Map<String, Object>) accruedDataRaw);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateCalcDataArray(Map<String, Object> calcDataMap) {
        Object itemsRaw = calcDataMap.get(FIELD_ITEMS);
        if (!(itemsRaw instanceof java.util.List)) {
            return createManualValidationError(FIELD_VALUE_CODE_CAMEL, "items must be a list");
        }
        java.util.List<?> items = (java.util.List<?>) itemsRaw;
        for (Object itemObj : items) {
            ResponseEntity<DefaultResponse> err = validateCalcDataItem(itemObj);
            if (err != null) return err;
        }
        return null;
    }

    private ResponseEntity<DefaultResponse> checkCalcItemUnknownFields(Map<String, Object> item) {
        for (String key : item.keySet()) {
            if (!ALLOWED_CALC_ITEM_FIELDS.contains(key)) {
                return createManualValidationError(FIELD_UNEXPECTED, MSG_UNEXPECTED_FIELD_PREFIX + key);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateCalcDataItem(Object itemObj) {
        if (!(itemObj instanceof Map)) {
            return createManualValidationError(FIELD_VALUE_CODE_CAMEL, "item must be an object");
        }
        Map<String, Object> item = (Map<String, Object>) itemObj;
        ResponseEntity<DefaultResponse> unknownErr = checkCalcItemUnknownFields(item);
        if (unknownErr != null) return unknownErr;
        if (!item.containsKey(FIELD_VALUE_CODE) || item.get(FIELD_VALUE_CODE) == null) {
            return createManualValidationError(FIELD_VALUE_CODE_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (!item.containsKey(FIELD_CALC_START_DATE) || item.get(FIELD_CALC_START_DATE) == null) {
            return createManualValidationError("calculationStartDate", MSG_MUST_NOT_BE_NULL);
        }
        if (!item.containsKey(FIELD_CALC_END_DATE) || item.get(FIELD_CALC_END_DATE) == null) {
            return createManualValidationError("calculationEndDate", MSG_MUST_NOT_BE_NULL);
        }
        Object vcRaw = item.get(FIELD_VALUE_CODE);
        if (vcRaw != null && !VALID_VALUE_CODES.contains(vcRaw.toString())) {
            return createManualValidationError(FIELD_VALUE_CODE_CAMEL, "invalid value_code");
        }
        Object csdRaw = item.get(FIELD_CALC_START_DATE);
        if (csdRaw != null && !CALC_ISO_DATETIME_PATTERN.matcher(csdRaw.toString()).matches()) {
            return createManualValidationError("calculationStartDate", MSG_INVALID_FORMAT);
        }
        Object cedRaw = item.get(FIELD_CALC_END_DATE);
        if (cedRaw != null && !CALC_ISO_DATETIME_PATTERN.matcher(cedRaw.toString()).matches()) {
            return createManualValidationError("calculationEndDate", MSG_INVALID_FORMAT);
        }
        return null;
    }
    
    @Override
    public ResponseEntity<DefaultResponse> viewResponseNumber(
            UUID xRequestID,
            Object viewResponseRequestNumberData) {
        
        log.info("Received view response request number with X-Request-ID: {}", xRequestID);
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getContentLengthLong() > 1_048_576L) {
            return createPayloadTooLargeResponse();
        }

        ViewResponseRequestNumberData request;
        try {
            request = convertToType(viewResponseRequestNumberData, ViewResponseRequestNumberData.class);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid data format: {}", e.getMessage());
            return createManualValidationError(extractFieldNameFromException(e), MSG_INVALID_VALUE);
        }

        // An empty body {} is a fundamentally invalid request
        if (isEmptyBody(viewResponseRequestNumberData)) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }

        // submission_id key must be present (value may be null per spec nullable:true)
        if (viewResponseRequestNumberData instanceof Map<?, ?> rawMap
                && !rawMap.containsKey(FIELD_SUBMISSION_ID)) {
            return createManualValidationError(FIELD_SUBMISSION_ID_CAMEL, MSG_MUST_NOT_BE_NULL);
        }

        ResponseEntity<DefaultResponse> err = validateNumberRequiredFields(request);
        if (err != null) return err;

        return validateAndProcess(request, "/view-response/request-number");
    }

    // Manual validation for required fields (because @Size on enum breaks validator)
    @SuppressWarnings({"java:S2583", "java:S3516"})
    private ResponseEntity<DefaultResponse> validateNumberRequiredFields(ViewResponseRequestNumberData request) {
        if (request.getHoldernameGuid() == null) {
            return createManualValidationError(FIELD_HOLDERNAME_GUID, MSG_MUST_NOT_BE_NULL);
        }
        if (request.getRecordId() == null) {
            return createManualValidationError(FIELD_RECORD_ID_CAMEL, MSG_MUST_NOT_BE_NULL);
        }

        if (request.getSubmissionStatus() == null) {
            return createManualValidationError(FIELD_SUBMISSION_STATUS_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (request.getReportingPeriodStart() == null) {
            return createManualValidationError(FIELD_REPORTING_PERIOD_START_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (request.getSubmissionReason() == null) {
            return createManualValidationError(FIELD_SUBMISSION_REASON_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (request.getReqCount() == null) {
            return createManualValidationError("reqCount", MSG_MUST_NOT_BE_NULL);
        }
        return null;
    }
    
private ResponseEntity<DefaultResponse> createPayloadTooLargeResponse() {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(ABOUT_BLANK);
        problemDetails.setTitle("Payload Too Large");
        problemDetails.setStatus(413);
        problemDetails.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        ProblemDetailsErrorsInner error = new ProblemDetailsErrorsInner();
        error.setCode("CONTENT_TOO_LARGE");
        problemDetails.setErrors(List.of(error));
        @SuppressWarnings("unchecked")
        ResponseEntity<DefaultResponse> response = (ResponseEntity<DefaultResponse>)(ResponseEntity<?>) new ResponseEntity<>(problemDetails, HttpStatus.PAYLOAD_TOO_LARGE);
        return response;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<DefaultResponse> viewResponseTime(
            UUID xRequestID,
            Object viewResponseTimeRequest) {

        log.info("Received view response time request with X-Request-ID: {}", xRequestID);
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getContentLengthLong() > 1_048_576L) {
            log.warn("Payload too large");
            return createPayloadTooLargeResponse();
        }
        if (!(viewResponseTimeRequest instanceof Map)) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }
        Map<String, Object> m = (Map<String, Object>) viewResponseTimeRequest;
        if (m.isEmpty()) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }

        Object submissionReasonRaw = m.get(FIELD_SUBMISSION_REASON);
        boolean isNullSubmission = VALUE_NULL_SUBMISSION.equals(submissionReasonRaw);

        ResponseEntity<DefaultResponse> err;
        if ((err = validateTimeNullSubmissionRules(m, isNullSubmission)) != null) return err;
        if ((err = validateCommonRequiredFields(m, isNullSubmission)) != null) return err;
        if (!isNullSubmission && (!m.containsKey(FIELD_ITEMS) || m.get(FIELD_ITEMS) == null)) {
            return createManualValidationError(FIELD_REQ_DATE_TS_CAMEL, "items must not be null");
        }
        if ((err = validateCommonFormats(m, submissionReasonRaw)) != null) return err;
        if (!isNullSubmission && (err = validateTimeItems(m)) != null) return err;

        // For null submissions: skip JSR-303 (items @Size(min=1) would fail on empty list)
        return completeTimeResponse(isNullSubmission, viewResponseTimeRequest);
    }

    private ResponseEntity<DefaultResponse> completeTimeResponse(
            boolean isNullSubmission, Object viewResponseTimeRequest) {
        if (isNullSubmission) {
            StubConfiguration config = configurationService.getAndDecrementConfiguration("/view-response/response-time");
            if (config != null) return createErrorResponse(config);
            DefaultResponse response = new DefaultResponse();
            response.setMessage(MSG_ACCEPTED);
            response.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        ViewResponseResponseTimesData request;
        try {
            request = convertToType(viewResponseTimeRequest, ViewResponseResponseTimesData.class);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid data format: {}", e.getMessage());
            return createManualValidationError(extractFieldNameFromException(e), MSG_INVALID_VALUE);
        }
        return validateAndProcess(request, "/view-response/response-time");
    }

    private ResponseEntity<DefaultResponse> validateTimeNullSubmissionRules(
            Map<String, Object> m, boolean isNullSubmission) {
        if (!isNullSubmission) return null;
        if (m.get(FIELD_SUBMISSION_ID) != null) {
            return createManualValidationError(FIELD_SUBMISSION_ID_CAMEL, "must be null for null submission");
        }
        if (m.containsKey(FIELD_ITEMS) && m.get(FIELD_ITEMS) != null) {
            return createManualValidationError(FIELD_UNEXPECTED, "null submission must not have items");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateTimeItems(Map<String, Object> m) {
        Object itemsRaw = m.get(FIELD_ITEMS);
        if (!(itemsRaw instanceof java.util.List)) {
            return createManualValidationError(FIELD_REQ_DATE_TS_CAMEL, "items must be a list");
        }
        java.util.List<?> items = (java.util.List<?>) itemsRaw;
        if (items.isEmpty()) {
            return createManualValidationError(FIELD_REQ_DATE_TS_CAMEL, "items must not be empty");
        }
        for (Object itemObj : items) {
            ResponseEntity<DefaultResponse> err = validateTimeItem(itemObj);
            if (err != null) return err;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<DefaultResponse> validateTimeItem(Object itemObj) {
        if (!(itemObj instanceof Map)) {
            return createManualValidationError(FIELD_REQ_DATE_TS_CAMEL, "item must be an object");
        }
        Map<String, Object> item = (Map<String, Object>) itemObj;
        for (String key : item.keySet()) {
            if (!ALLOWED_TIME_ITEM_FIELDS.contains(key)) {
                return createManualValidationError(FIELD_UNEXPECTED, MSG_UNEXPECTED_FIELD_PREFIX + key);
            }
        }
        if (!item.containsKey(FIELD_REQ_DATE_TS) || item.get(FIELD_REQ_DATE_TS) == null) {
            return createManualValidationError(FIELD_REQ_DATE_TS_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        if (!item.containsKey(FIELD_RESP_DATE_TS) || item.get(FIELD_RESP_DATE_TS) == null) {
            return createManualValidationError(FIELD_RESP_DATE_TS_CAMEL, MSG_MUST_NOT_BE_NULL);
        }
        Object reqDateTsRaw = item.get(FIELD_REQ_DATE_TS);
        if (reqDateTsRaw != null && !CALC_ISO_DATETIME_PATTERN.matcher(reqDateTsRaw.toString()).matches()) {
            return createManualValidationError(FIELD_REQ_DATE_TS_CAMEL, MSG_INVALID_FORMAT);
        }
        Object respDateTsRaw = item.get(FIELD_RESP_DATE_TS);
        if (respDateTsRaw != null && !CALC_ISO_DATETIME_PATTERN.matcher(respDateTsRaw.toString()).matches()) {
            return createManualValidationError(FIELD_RESP_DATE_TS_CAMEL, MSG_INVALID_FORMAT);
        }
        return null;
    }
    
@SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<DefaultResponse> viewResponseUnavailable(
            UUID xRequestID,
            Object viewResponseUnavailableRequest) {

        log.info("Received view response unavailable request with X-Request-ID: {}", xRequestID);
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getContentLengthLong() > 1_048_576L) {
            return createPayloadTooLargeResponse();
        }
        if (!(viewResponseUnavailableRequest instanceof Map)) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }
        Map<String, Object> m = (Map<String, Object>) viewResponseUnavailableRequest;
        if (m.isEmpty()) {
            return createManualValidationError(FIELD_REQUEST_BODY, MSG_MUST_NOT_BE_EMPTY);
        }
        for (String key : m.keySet()) {
            if (!ALLOWED_UNAVAIL_FIELDS.contains(key)) {
                return createManualValidationError(FIELD_UNEXPECTED, MSG_UNEXPECTED_FIELD_PREFIX + key);
            }
        }

        Object submissionReasonRaw = m.get(FIELD_SUBMISSION_REASON);
        ResponseEntity<DefaultResponse> err;
        // unavailable always requires submission_id (no null-submission variant)
        if ((err = validateCommonRequiredFields(m, false)) != null) return err;
        if ((err = validateCommonFormats(m, submissionReasonRaw)) != null) return err;
        if ((err = validateUnavailCounts(m)) != null) return err;

        StubConfiguration config = configurationService.getAndDecrementConfiguration("/view-response/unavailable");
        if (config != null) {
            log.info("Returning stubbed error response for /view-response/unavailable with status {} and code {}",
                    config.getStatus(), config.getCode());
            return createErrorResponse(config);
        }
        DefaultResponse response = new DefaultResponse();
        response.setMessage(MSG_ACCEPTED);
        response.setDatetimestamp(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private ResponseEntity<DefaultResponse> validateUnavailCounts(Map<String, Object> m) {
        ResponseEntity<DefaultResponse> err;
        if ((err = validateUnavailCount(m, "contact_count", "contactCount")) != null) return err;
        if ((err = validateUnavailCount(m, "missingadmin_count", "missingadminCount")) != null) return err;
        if ((err = validateUnavailCount(m, "temperror_count", "temperrorCount")) != null) return err;
        if ((err = validateUnavailCount(m, "eri_unavail_ano_count", "eriUnavailAnoCount")) != null) return err;
        if ((err = validateUnavailCount(m, "eri_unavail_ppf_count", "eriUnavailPpfCount")) != null) return err;
        if ((err = validateUnavailCount(m, "eri_unavail_trn_count", "eriUnavailTrnCount")) != null) return err;
        if ((err = validateUnavailCount(m, "accrued_unavail_ano_count", "accruedUnavailAnoCount")) != null) return err;
        if ((err = validateUnavailCount(m, "accrued_unavail_ppf_count", "accruedUnavailPpfCount")) != null) return err;
        if ((err = validateUnavailCount(m, "accrued_unavail_trn_count", "accruedUnavailTrnCount")) != null) return err;
        return null;
    }

    private ResponseEntity<DefaultResponse> validateUnavailCount(Map<String, Object> item, String jsonKey, String fieldName) {
        if (!item.containsKey(jsonKey) || item.get(jsonKey) == null) {
            return createManualValidationError(fieldName, MSG_MUST_NOT_BE_NULL);
        }
        Object val = item.get(jsonKey);
        if (!(val instanceof Number number) || number.intValue() < 0) {
            return createManualValidationError(fieldName, MSG_INVALID_VALUE);
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
