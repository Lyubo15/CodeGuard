package com.tu.codeguard.exceptions.handler;

import com.tu.codeguard.exceptions.BaseException;
import jakarta.validation.ConstraintViolationException;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for defining centralized exception handling for the REST API.
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"NullableProblems", "unused"})
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    // Override default framework Exception handlers
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String[] errorDetails = collectBindingErrors(ex);
        ApiError apiError = createApiError(ex, ErrorCode.INVALID_PARAMETER,
                new Object[]{ex.getParameter().getParameterName()}, errorDetails);
        return handleExceptionInternal(ex, apiError, headers, apiError.getErrorCode().getHttpStatus(), request);
    }

    /**
     * Create an API error.
     *
     * @param ex                  main exception used for setting the developer-friendly message.
     * @param errorCode           unified code for the error, also used for setting the HTTP code and user message.
     * @param errorCodeParameters parameters to be placed in the user message.
     * @param errorDetails        additional developer-friendly error details to aid debugging.
     * @return API error.
     */
    private ApiError createApiError(Exception ex, ErrorCode errorCode, Object[] errorCodeParameters,
                                    String... errorDetails) {
        ApiError apiError = new ApiError(errorCode,
                ex.getMessage(),
                messageSource.getMessage(
                        errorCode.name(),
                        errorCodeParameters,
                        LocaleContextHolder.getLocale()),
                errorDetails);
        log.warn("Exception " + apiError.getExceptionId() + ": " + ex.getMessage(), ex);

        return apiError;
    }

    /**
     * Create an API error. Syntactic sugar which skips defining developer-friendly error details (which are used rarely).
     * See {@link #createApiError(Exception, ErrorCode, Object[], String...)}
     */
    private ApiError createApiError(Exception ex, ErrorCode errorCode,
                                    Object... errorCodeParameters) {
        return createApiError(ex, errorCode, errorCodeParameters,
                new String[0]); // use exception message for details
    }

    private String[] collectBindingErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        errors.addAll(
                ex.getBindingResult().getFieldErrors().stream()
                        .map(e -> e.getField() + " : " + e.getDefaultMessage())
                        .toList());

        errors.addAll(
                ex.getBindingResult().getGlobalErrors().stream()
                        .map(e -> e.getObjectName() + " : " + e.getDefaultMessage())
                        .toList());
        return errors.toArray(new String[0]);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiError apiError = createApiError(ex, ErrorCode.MISSING_PARAMETER, ex.getParameterName(),
                ex.getParameterType());

        return handleExceptionInternal(ex, apiError, headers, apiError.getErrorCode().getHttpStatus(), request);
    }

    // Framework Exception handlers

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        if (ex.getCause() instanceof BaseException baseExceptionCause) {
            return handleBaseException(baseExceptionCause);
        }
        ApiError apiError = createApiError(ex, ErrorCode.VALIDATION_FAILURE);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException(
            ConstraintViolationException ex) {
        String[] errorDetails = collectConstraintViolations(ex);
        ApiError apiError = createApiError(ex, ErrorCode.CONSTRAINT_VIOLATION, new Object[0],
                errorDetails);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    private String[] collectConstraintViolations(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(v ->
                        v.getRootBeanClass().getName()
                                + " "
                                + v.getPropertyPath()
                                + ": "
                                + v.getMessage())
                .toArray(String[]::new);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = createApiError(ex, ErrorCode.UNAUTHENTICATED);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError apiError = createApiError(ex, ErrorCode.ACCESS_DENIED);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({DateTimeException.class})
    public ResponseEntity<ApiError> handleDateTimeException(DateTimeException ex) {
        ApiError apiError = createApiError(ex, ErrorCode.INVALID_DATE);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        ApiError apiError = createApiError(ex, ErrorCode.TYPE_MISMATCH, ex.getName(),
                requiredType != null ? requiredType.getName() : "unknown");
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<ApiError> handleMultipartException(MultipartException ex) {
        String[] errorDetails = parseMultipartExceptionMessage(ex);
        ApiError apiError = createApiError(ex, ErrorCode.DEFAULT_ERROR, new Object[0], errorDetails);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    private String[] parseMultipartExceptionMessage(MultipartException ex) {
        String[] messages = new String[2];
        messages[0] = ex.getLocalizedMessage().split(";")[0];
        String[] reason = ex.getCause().getLocalizedMessage().split(": ");
        messages[1] = reason.length > 1 ? reason[1] : reason[0];
        return messages;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = createApiError(ex, ErrorCode.ILLEGAL_ARGUMENT);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = createApiError(ex, ErrorCode.DEFAULT_ERROR);
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    /**
     * Main application exception handling.
     */
    @ExceptionHandler({BaseException.class})
    public ResponseEntity<ApiError> handleBaseException(BaseException ex) {
        ApiError apiError = createApiError(ex, ex.getErrorCode(), (Object[]) ex.getErrorCodeParameters());
        return new ResponseEntity<>(apiError, apiError.getErrorCode().getHttpStatus());
    }

    /**
     * Override main error handling method to ensure unexpected errors are serialized as ApiError-s too.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        ApiError responseBody;
        if (body instanceof ApiError apiErrorBody) {
            responseBody = apiErrorBody;
        } else {
            // catch all error handler
            responseBody = createApiError(ex, ErrorCode.DEFAULT_ERROR);
        }

        return super.handleExceptionInternal(ex, responseBody, headers, statusCode, request);
    }

}
