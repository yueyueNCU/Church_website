package org.kangning.church.common.exception.auth;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException() {
        super("密碼錯誤");
    }
}
