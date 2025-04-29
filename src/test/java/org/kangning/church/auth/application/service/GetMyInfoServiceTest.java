package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMyInfoServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetMyInfoService getMyInfoService;

    @Test
    void getMyInfo_success_should_return_user_info() {
        User mockUser = new User(
                new UserId(1L),
                "john",
                "encoded-password",
                Set.of(Role.SITE_ADMIN)
        );
        when(userRepositoryPort.findByUsername("john"))
                .thenReturn(Optional.of(mockUser));

        // Act
        var response = getMyInfoService.getMyInfo("john");

        // Assert
        assertEquals(mockUser.getId(), response.id());
        assertEquals(mockUser.getUsername(), response.username());
        assertEquals(mockUser.getGlobalRoles(), response.globalRoles());
    }
    @Test
    void getMyInfo_user_not_exist_should_return_exception(){
        when(userRepositoryPort.findByUsername("notexist")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(UserNotFoundException.class, () -> {
            getMyInfoService.getMyInfo("notexist");
        });
    }
}