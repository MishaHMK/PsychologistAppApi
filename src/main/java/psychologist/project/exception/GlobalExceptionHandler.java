package psychologist.project.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccessErrors(
            EntityNotFoundException ex) {
        return new ResponseEntity<>(buildExceptionResponse(ex),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<Map<String, Object>> handleAccessErrors(
            AccessException ex) {
        return new ResponseEntity<>(buildExceptionResponse(ex),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleAccessErrors(
            RegistrationException ex) {
        return new ResponseEntity<>(buildExceptionResponse(ex),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<Map<String, Object>> handleAccessErrors(
            BookingException ex) {
        return new ResponseEntity<>(buildExceptionResponse(ex),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentException(
            PaymentException ex) {
        return new ResponseEntity<>(buildExceptionResponse(ex),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(
            SecurityException ex) {
        return new ResponseEntity<>(buildExceptionResponse(ex),
                HttpStatus.FORBIDDEN);
    }

    private Map<String, Object> buildExceptionResponse(Throwable ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getMessage());
        return body;
    }
}
