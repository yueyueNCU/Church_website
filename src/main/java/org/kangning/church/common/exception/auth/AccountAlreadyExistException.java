package org.kangning.church.common.exception.auth;

public class AccountAlreadyExistException extends RuntimeException{
    public AccountAlreadyExistException() {
        super("帳號已存在");
    }
}
