package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
        User user = new User("john", "encoded-password", List.of(Role.LEADER, Role.MEMBER));
        when(userRepositoryPort.findByUsername("john"))
                .thenReturn(Optional.of(user));

        var response = getMyInfoService.getMyInfo("john");

        assertEquals("john", response.username());
        assertEquals(List.of(Role.LEADER, Role.MEMBER), response.roles());
    }
    @Test
    void getMyInfo_user_not_exist_should_return_exception(){
        when(userRepositoryPort.findByUsername("notexist")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            getMyInfoService.getMyInfo("notexist");
        });
        assertEquals("使用者不存在", thrown.getMessage());
    }
}