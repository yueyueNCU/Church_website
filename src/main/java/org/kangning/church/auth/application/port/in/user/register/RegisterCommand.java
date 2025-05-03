package org.kangning.church.auth.application.port.in.user.register;

import org.kangning.church.common.exception.auth.PasswordMismatchException;

public record RegisterCommand(
        String username,
        String account,
        String password
) {
    public static RegisterCommand of(String username,String account, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
        return new RegisterCommand(username, account, password);
    }
}
