package org.kangning.church.membership.adaptor.in;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.kangning.church.auth.adapter.in.security.UserPrincipal;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.adaptor.in.dto.MembersResponse;
import org.kangning.church.membership.adaptor.in.dto.UpdateMembershipRoleRequest;
import org.kangning.church.membership.application.port.in.MemberResult;
import org.kangning.church.membership.application.port.in.MembershipUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/church/{churchId}/members")
@AllArgsConstructor
public class MembershipController {

    private final MembershipUseCase membershipUseCase;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_LEADER') or hasRole('ROLE_SITE_ADMIN')")
    public ResponseEntity<List<MembersResponse>> getChurchMembers(
            @PathVariable ChurchId churchId
    ){
        List<MemberResult> result = membershipUseCase.getChurchMembers(churchId);

        List<MembersResponse> response = result.stream()
                .map(r -> new MembersResponse(
                        r.userId(),
                        r.username(),
                        r.roles(),
                        r.status()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<MembersResponse> getMyMembership(
            @PathVariable ChurchId churchId,
            Authentication authentication
    ){
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        MemberResult result= membershipUseCase.getMyMembership(churchId,principal.id());

        var response = new MembersResponse(result.userId(),result.username(),result.roles(),result.status());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/apply")
    public ResponseEntity<Void> applyMembership(
            @PathVariable ChurchId churchId,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        membershipUseCase.applyMembership(principal.id(), churchId);

        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/approve")
    @PreAuthorize("hasRole('ROLE_LEADER') or hasRole('ROLE_SITE_ADMIN')")
    public ResponseEntity<Void> approveMembership(
            @PathVariable ChurchId churchId,
            @PathVariable UserId userId
    ) {
        membershipUseCase.approveMembership(churchId, userId);

        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/reject")
    @PreAuthorize("hasRole('ROLE_LEADER') or hasRole('ROLE_SITE_ADMIN')")
    public ResponseEntity<Void> rejectMembership(
            @PathVariable ChurchId churchId,
            @PathVariable UserId userId
    ) {
        membershipUseCase.rejectMembership(churchId, userId);

        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ROLE_LEADER') or hasRole('ROLE_SITE_ADMIN')")
    public ResponseEntity<Void> updateMembershipRole(
            @PathVariable Long churchId,
            @PathVariable Long userId,
            @RequestBody @Valid UpdateMembershipRoleRequest request
    ) {
        membershipUseCase.updateMembershipRole(
                new ChurchId(churchId),
                new UserId(userId),
                request.roles()
        );

        return ResponseEntity.noContent().build(); // 204 No Content
    }
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_LEADER') or hasRole('ROLE_SITE_ADMIN')")
    public ResponseEntity<MembersResponse> getMembership(
            @PathVariable ChurchId churchId,
            @PathVariable UserId userId
    ) {
        MemberResult result = membershipUseCase.getMyMembership(churchId, userId);

        return ResponseEntity.ok(new MembersResponse(
                result.userId(),
                result.username(),
                result.roles(),
                result.status()
        ));
    }
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_LEADER') or hasRole('ROLE_SITE_ADMIN')")
    public ResponseEntity<Void> removeMembership(
            @PathVariable ChurchId churchId,
            @PathVariable UserId userId
    ) {
        membershipUseCase.removeMembership(churchId, userId);

        return ResponseEntity.noContent().build();
    }
}
