package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.ChurchRole;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.UserNotFoundException;
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
        User mockUser = new User("john",
                "encoded-password",
                List.of(),
                List.of(new ChurchRole(1L, List.of(Role.LEADER)))
        );
        when(userRepositoryPort.findByUsername("john"))
                .thenReturn(Optional.of(mockUser));

        var response = getMyInfoService.getMyInfo("john");

        assertEquals("john", response.username()); // 確認使用者名稱
        assertTrue(response.globalRoles().isEmpty()); // 確認global roles是空的
        assertEquals(1, response.userChurchRoles().size()); // 確認有一個教會
        assertEquals(1L, response.userChurchRoles().getFirst().churchId()); // 確認教會ID是1
        assertEquals(List.of(Role.LEADER), response.userChurchRoles().getFirst().roles()); // 確認角色是 LEADER
    }
    @Test
    void getMyInfo_user_not_exist_should_return_exception(){
        when(userRepositoryPort.findByUsername("notexist")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(UserNotFoundException.class, () -> {
            getMyInfoService.getMyInfo("notexist");
        });
    }
}