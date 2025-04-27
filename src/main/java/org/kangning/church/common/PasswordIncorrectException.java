package org.kangning.church.common;

public class PasswordIncorrectException extends RuntimeException {
    public PasswordIncorrectException() {
        super("密碼錯誤");
    }
}
