package org.kangning.church.common.exception.auth;

public class OldPasswordIncorrectException extends RuntimeException {
    public OldPasswordIncorrectException() {
        super("舊密碼錯誤");
    }
}
