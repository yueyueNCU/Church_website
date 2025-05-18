package org.kangning.church.churchRole.adapter.in;

import org.kangning.church.churchRole.adapter.in.dto.PermissionResponse;
import org.kangning.church.churchRole.domain.Permission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class PermissionController {

    @GetMapping("/api/permissions")
    public List<PermissionResponse> getAllPermissions() {
        return Arrays.stream(Permission.values())
                .map(p -> new PermissionResponse(p.name(), p.getGroup().name(), p.getDescription()))
                .toList();
    }
}