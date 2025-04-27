package org.kangning.church.common;

public class NewPasswordSameAsOldException extends RuntimeException {
    public NewPasswordSameAsOldException() {
        super("新密碼不能與舊密碼相同");
    }
}