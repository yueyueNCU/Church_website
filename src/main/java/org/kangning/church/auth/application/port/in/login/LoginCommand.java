package org.kangning.church.auth.application.port.in.login;

public record LoginCommand(String username, String rawPassword) {
}
