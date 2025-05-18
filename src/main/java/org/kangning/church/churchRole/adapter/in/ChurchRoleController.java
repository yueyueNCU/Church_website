package org.kangning.church.churchRole.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.churchRole.adapter.in.dto.ChurchRoleResponse;
import org.kangning.church.churchRole.adapter.in.dto.CreateChurchRoleRequest;
import org.kangning.church.churchRole.adapter.in.dto.RenameChurchRoleRequest;
import org.kangning.church.churchRole.adapter.in.dto.UpdatePermissionsRequest;
import org.kangning.church.churchRole.application.port.in.ChurchRoleUseCase;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/church/{churchId}/roles")
@RequiredArgsConstructor
public class ChurchRoleController {

    private final ChurchRoleUseCase churchRoleUseCase;

    /** 建立角色 */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('PERM_ADD_ROLE','PERM_ALL_PERMISSION') or hasRole('SITE_ADMIN')")
    public ResponseEntity<ChurchRoleResponse> createRole(
            @PathVariable Long churchId,
            @RequestBody @Valid CreateChurchRoleRequest request
    ) {
        ChurchRole role = churchRoleUseCase.createChurchRole(
                new ChurchId(churchId),
                request.name(),
                request.isDefault()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChurchRoleResponse.from(role));
    }

    /** 查詢全部角色 */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('PERM_VIEW_ROLES','PERM_ALL_PERMISSION') or hasRole('SITE_ADMIN')")
    public ResponseEntity<List<ChurchRoleResponse>> getAllRoles(
            @PathVariable Long churchId
    ) {
        Set<ChurchRole> roles = churchRoleUseCase.getChurchRoles(new ChurchId(churchId));
        return ResponseEntity.ok(
                roles.stream().map(ChurchRoleResponse::from).toList()
        );
    }

    /** 查詢 default 角色 */
    @GetMapping("/default")
    @PreAuthorize("hasAnyAuthority('PERM_VIEW_ROLES','PERM_ALL_PERMISSION') or hasRole('SITE_ADMIN')")
    public ResponseEntity<List<ChurchRoleResponse>> getDefaultRoles(
            @PathVariable Long churchId
    ) {
        Set<ChurchRole> roles = churchRoleUseCase.getDefaultRoles(new ChurchId(churchId));
        return ResponseEntity.ok(
                roles.stream().map(ChurchRoleResponse::from).toList()
        );
    }

    /** 刪除角色 */
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('PERM_DELETE_ROLE','PERM_ALL_PERMISSION') or hasRole('SITE_ADMIN')")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Long roleId
    ) {
        churchRoleUseCase.deleteChurchRole(new ChurchRoleId(roleId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasAnyAuthority('PERM_UPDATE_ROLE','PERM_ALL_PERMISSION') or hasRole('SITE_ADMIN')")
    public ResponseEntity<Void> replacePermissions(
            @PathVariable Long roleId,
            @RequestBody UpdatePermissionsRequest request
    ) {
        churchRoleUseCase.updateRolePermissions(new ChurchRoleId(roleId), request.permissions());
        return ResponseEntity.ok().build();
    }

    /** 修改角色名稱 */
    @PatchMapping("/{roleId}/name")
    @PreAuthorize("hasAnyAuthority('PERM_RENAME_ROLE','PERM_ALL_PERMISSION') or hasRole('SITE_ADMIN')")
    public ResponseEntity<Void> renameRole(
            @PathVariable Long roleId,
            @RequestBody RenameChurchRoleRequest request
    ) {
        churchRoleUseCase.renameChurchRole(new ChurchRoleId(roleId), request.newName());
        return ResponseEntity.ok().build();
    }
}