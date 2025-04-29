package org.kangning.church.common.exception.auth;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("使用者不存在");
    }
}
