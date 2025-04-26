package org.kangning.church.auth.application.port.in.user.dto;

public record UpdatePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
}
