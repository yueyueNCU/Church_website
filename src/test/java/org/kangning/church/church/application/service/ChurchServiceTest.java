package org.kangning.church.church.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.application.port.in.ChurchResult;
import org.kangning.church.church.application.port.in.CreateChurchCommand;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.exception.church.ChurchNameDuplicateException;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChurchServiceTest {
    @Mock
    private ChurchRepositoryPort churchRepository;
    @Mock
    private MembershipRepositoryPort membershipRepository;

    @InjectMocks
    private ChurchService churchService;

    @Test
    void createChurch_success_should_return_church(){
        // Arrange
        when(churchRepository.existsByName("康寧街教會")).thenReturn(false);
        Church savedChurch = new Church(new ChurchId(42L), "康寧街教會", "台北", "建立於19xx", Instant.now());
        when(churchRepository.save(any())).thenReturn(savedChurch);

        CreateChurchCommand cmd = new CreateChurchCommand("康寧街教會", "台北", "建立於19xx");
        UserId userId = new UserId(1L);

        // Act
        Church result = churchService.createChurch(userId, cmd);

        // Assert
        assertEquals("康寧街教會", result.getName());
        verify(churchRepository).save(any(Church.class));

        // 檢查是否建立 membership 並為 LEADER
        ArgumentCaptor<Membership> captor = ArgumentCaptor.forClass(Membership.class);
        verify(membershipRepository).save(captor.capture());
        Membership savedMembership = captor.getValue();
        assertEquals(userId, savedMembership.getUserId());
        assertEquals(savedChurch.getId(), savedMembership.getChurchId());
        assertTrue(savedMembership.getRoles().contains(Role.LEADER));
        assertEquals(ChurchMemberStatus.APPROVED, savedMembership.getStatus());
    }

    @Test
    void createChurch_whenNameExists_shouldThrowException() {
        // Arrange
        when(churchRepository.existsByName("康寧街教會")).thenReturn(true);
        CreateChurchCommand cmd = new CreateChurchCommand("康寧街教會", "台北", "建立於19xx");
        UserId userId = new UserId(1L);

        // Act & Assert
        assertThrows(ChurchNameDuplicateException.class, () -> {
            churchService.createChurch(userId, cmd);
        });

        verify(churchRepository, never()).save(any());
        verify(membershipRepository, never()).save(any());
    }

    @Test
    void getMyChurches_shouldReturnChurchList() {
        // Arrange
        UserId userId = new UserId(1L);
        ChurchId ch1 = new ChurchId(10L);
        ChurchId ch2 = new ChurchId(20L);

        List<Membership> memberships = List.of(
                new Membership(null, ch1, userId, Set.of(Role.MEMBER), ChurchMemberStatus.APPROVED),
                new Membership(null, ch2, userId, Set.of(Role.LEADER), ChurchMemberStatus.APPROVED)
        );
        when(membershipRepository.findApprovedByUserId(userId)).thenReturn(memberships);

        Church church1 = new Church(ch1, "教會 A", "地址 A", "描述 A", Instant.now());
        Church church2 = new Church(ch2, "教會 B", "地址 B", "描述 B", Instant.now());
        when(churchRepository.findAllByIds(List.of(ch1, ch2)))
                .thenReturn(List.of(church1, church2));

        // Act
        List<ChurchResult> results = churchService.getMyChurches(userId);

        // Assert
        assertEquals(2, results.size());
        assertEquals("教會 A", results.get(0).name());
        assertEquals("教會 B", results.get(1).name());
    }

    @Test
    void searchByNameContaining_shouldReturnMatchedChurches() {
        // Arrange
        ChurchId ch1 = new ChurchId(100L);
        ChurchId ch2 = new ChurchId(101L);

        List<Church> fakeResult = List.of(
                new Church(ch1, "台北教會", "中山路", "北區分堂", Instant.now()),
                new Church(ch2, "台中教會", "建國路", "中區分堂", Instant.now())
        );

        when(churchRepository.searchByNameContaining("教會", 10, 0))
                .thenReturn(fakeResult);

        // Act
        List<ChurchResult> results = churchService.searchByNameContaining("教會", 10, 0);

        // Assert
        assertEquals(2, results.size());
        assertEquals("台北教會", results.get(0).name());
        assertEquals("台中教會", results.get(1).name());
    }
}