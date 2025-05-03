package org.kangning.church.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.kangning.church.common.exception.auth.*;
import org.kangning.church.common.exception.church.ChurchNameDuplicateException;
import org.kangning.church.common.exception.church.ChurchNotFoundException;
import org.kangning.church.common.exception.church.UserAlreadyJoinedChurchException;
import org.kangning.church.common.exception.membership.ChurchApplicantNotExistException;
import org.kangning.church.common.exception.membership.MembershipAlreadyExistsException;
import org.kangning.church.common.exception.membership.MembershipNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Controller @Valid Exception
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

    //Account Exception
    @ExceptionHandler(AccountAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleAccountAlreadyExist(AccountAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("ACCOUNT_ALREADY_EXIST", e.getMessage()));
    }


    //Password Exception
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("USER_NOT_FOUND", e.getMessage()));
    }

    //Church Exception
    @ExceptionHandler(ChurchNameDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleChurchNameDuplicate(ChurchNameDuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CHURCH_NAME_DUPLICATE", e.getMessage()));
    }
    @ExceptionHandler(ChurchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChurchNotFound(ChurchNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("CHURCH_NOT_FOUND", e.getMessage()));
    }
    @ExceptionHandler(UserAlreadyJoinedChurchException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyJoinedChurch(UserAlreadyJoinedChurchException e){
        return ResponseEntity.badRequest().body(new ErrorResponse("USER_ALREADY_JOINED_CHURCH", e.getMessage()));
    }
    //Membership Exception
    @ExceptionHandler(ChurchApplicantNotExistException.class)
    public ResponseEntity<ErrorResponse> handleChurchApplicantNotExist(ChurchApplicantNotExistException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("CHURCH_APPLICANT_NOT_EXIST", e.getMessage()));
    }
    @ExceptionHandler(MembershipAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMembershipAlreadyExists(MembershipAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("MEMBERSHIP_ALREADY_EXISTS", e.getMessage()));
    }
    @ExceptionHandler(MembershipNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMembershipNotFound(MembershipNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("MEMBERSHIP_NOT_FOUND", e.getMessage()));
    }


    //missing argument exception
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("MISSING_PARAMETER", e.getParameterName() + " 參數缺失"));
    }
}