package org.kangning.church.auth.application.port.in.login;

public record LoginCommand(String account, String rawPassword) {
}
