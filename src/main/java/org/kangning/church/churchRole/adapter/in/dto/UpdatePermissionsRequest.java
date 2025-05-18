package org.kangning.church.churchRole.adapter.in.dto;

import org.kangning.church.churchRole.domain.Permission;

import java.util.Set;

public record UpdatePermissionsRequest(Set<Permission> permissions) {}