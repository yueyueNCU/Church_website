package org.kangning.church.auth.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.adapter.in.dto.password.UpdatePasswordRequest;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.testutil.TestJwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryPort userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserId userId;

    @BeforeEach
    void seedUser() {
        userRepository.deleteByAll();
        User user=userRepository.save(new User(
                null,
                "john",
                "TestAccount",
                passwordEncoder.encode("12345678"),
                Set.of(Role.LEADER)));
        userId=user.getId();
    }

    @Test
    void getMyInfo_withInvalidToken_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyInfo_withValidToken_shouldReturnOk() throws Exception {
        String token = TestJwtProvider.generateToken(
                userId,
                "john",
                Set.of(Role.LEADER)
        );

        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    @Test
    void updatePassword_withoutToken_shouldReturnUnauthorized() throws Exception{
        mockMvc.perform(put("/api/user/password"))
                    .andExpect(status().isUnauthorized());
    }
    @Test
    void updatePassword_withValidToken_shouldReturnOk() throws Exception {
        String token = TestJwtProvider.generateToken(
                userId,
                "john",
                Set.of(Role.LEADER)
        );

        var request = new UpdatePasswordRequest(
                "12345678",
                "newPassword123",
                "newPassword123"
        );

        mockMvc.perform(put("/api/user/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}