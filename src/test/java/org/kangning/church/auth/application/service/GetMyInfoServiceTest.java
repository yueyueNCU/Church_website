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
                "TestAccount",
                "encoded-password",
                null
        );
        when(userRepositoryPort.findById(new UserId(1L)))
                .thenReturn(Optional.of(mockUser));

        // Act
        var response = getMyInfoService.getMyInfo(new UserId(1L));

        // Assert
        assertEquals(mockUser.getId(), response.id());
        assertEquals(mockUser.getUsername(), response.username());
        assertEquals(mockUser.getGlobalRoles(), response.globalRoles());
    }
    @Test
    void getMyInfo_user_not_exist_should_return_exception(){
        when(userRepositoryPort.findById(new UserId(0L))).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(UserNotFoundException.class, () -> {
            getMyInfoService.getMyInfo(new UserId(0L));
        });
    }
}