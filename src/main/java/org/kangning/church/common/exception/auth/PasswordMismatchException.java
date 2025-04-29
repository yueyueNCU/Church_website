package org.kangning.church.common.exception.auth;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("新密碼與確認密碼不一致");
    }
}