package org.kangning.church.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 只拿第一個錯誤來回應（如果有很多錯誤，只拿第一個）
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + "：" + fieldError.getDefaultMessage())
                .orElse("參數驗證失敗");

        return ResponseEntity.badRequest().body(new ErrorResponse(
                "VALIDATION_ERROR",
                errorMessage
        ));
    }

    @ExceptionHandler(OldPasswordIncorrectException.class)
    public ResponseEntity<ErrorResponse> handleOldPasswordIncorrect(OldPasswordIncorrectException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("OLD_PASSWORD_INCORRECT", e.getMessage()));
    }

    @ExceptionHandler(NewPasswordSameAsOldException.class)
    public ResponseEntity<ErrorResponse> handleNewPasswordSameAsOld(NewPasswordSameAsOldException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("NEW_PASSWORD_SAME_AS_OLD", e.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMismatch(PasswordMismatchException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("PASSWORD_MISMATCH", e.getMessage()));
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorResponse> handlePasswordIncorrect(PasswordIncorrectException e){
        return ResponseEntity.badRequest().body(new ErrorResponse("PASSWORD_INCORRECT", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e){
        return ResponseEntity.badRequest().body(new ErrorResponse("USER_NOT_FOUND", e.getMessage()));
    }
}