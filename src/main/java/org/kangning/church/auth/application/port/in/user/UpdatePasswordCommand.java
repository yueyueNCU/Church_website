package org.kangning.church.auth.application.port.in.user;

public record UpdatePasswordCommand(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
