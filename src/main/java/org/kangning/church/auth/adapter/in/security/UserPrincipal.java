package org.kangning.church.auth.adapter.in.security;

import org.kangning.church.common.identifier.UserId;

public record UserPrincipal(UserId id, String username) {}