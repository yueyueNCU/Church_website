package org.kangning.church.membership.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.churchRole.domain.Permission;
import org.kangning.church.common.exception.membership.MembershipAlreadyExistsException;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @Mock
    private MembershipRepositoryPort membershipRepository;
    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private MembershipService service;

    private final UserId userId = new UserId(1L);
    private final ChurchId churchId = new ChurchId(1L);


    @Test
    void applyMembership_shouldSaveWhenNotExists() {
        when(membershipRepository.findByChurchIdAndUserId(churchId, userId)).thenReturn(Optional.empty());

        service.applyMembership(userId, churchId);

        verify(membershipRepository).save(any(Membership.class));
    }

    @Test
    void applyMembership_shouldThrowWhenExists() {
        when(membershipRepository.findByChurchIdAndUserId(churchId, userId)).thenReturn(Optional.of(mock(Membership.class)));

        assertThrows(MembershipAlreadyExistsException.class, () -> service.applyMembership(userId, churchId));
    }

    @Test
    void approveMembership_shouldUpdateWhenPending() {
        Membership pending = Membership.pending(churchId, userId);
        when(membershipRepository.findByChurchIdAndUserId(churchId, userId)).thenReturn(Optional.of(pending));

        service.approveMembership(churchId, userId);

        verify(membershipRepository).save(pending);
        assertTrue(pending.isApproved());
    }

    @Test
    void rejectMembership_shouldDeleteIfPending() {
        Membership pending = Membership.pending(churchId, userId);
        when(membershipRepository.findByChurchIdAndUserId(churchId, userId)).thenReturn(Optional.of(pending));

        service.rejectMembership(churchId, userId);

        verify(membershipRepository).deleteByChurchIdAndUserId(churchId, userId);
    }

    @Test
    void getMyMembership_shouldReturnResult() {
        Membership member = Membership.pending(churchId, userId);
        when(membershipRepository.findByChurchIdAndUserId(churchId, userId)).thenReturn(Optional.of(member));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId, "john", "acc", "pwd", Set.of())));

        var result = service.getMyMembership(churchId, userId);

        assertEquals("john", result.username());
        assertEquals(userId, result.userId());
    }

    @Test
    void getChurchMembers_shouldReturnApprovedList() {

        ChurchRole leaderRole = new ChurchRole(
                new ChurchRoleId(99L),
                churchId,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );

        Membership m = new Membership(null, churchId, userId, Set.of(leaderRole), ChurchMemberStatus.APPROVED);
        when(membershipRepository.findAllByChurchId(churchId)).thenReturn(List.of(m));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId, "john", "acc", "pwd", Set.of())));

        var members = service.getChurchMembers(churchId);

        assertEquals(1, members.size());
        assertEquals("john", members.get(0).username());
    }
}