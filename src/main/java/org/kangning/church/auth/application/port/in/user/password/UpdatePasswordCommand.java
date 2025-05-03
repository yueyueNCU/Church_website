package org.kangning.church.auth.application.port.in.user.password;

public record UpdatePasswordCommand(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
