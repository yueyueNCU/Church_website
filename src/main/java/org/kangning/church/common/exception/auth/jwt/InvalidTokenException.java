package org.kangning.church.common.exception.auth.jwt;

public class  InvalidTokenException extends RuntimeException {
    public  InvalidTokenException() {
        super("JWT 無效或過期");
    }
}