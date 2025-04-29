package org.kangning.church.auth.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.adapter.in.dto.password.UpdatePasswordRequest;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.testutil.TestJwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void seedUser() {
        userRepositoryPort.deleteByAll();
        userRepositoryPort.save(new User(
                null, "john",
                passwordEncoder.encode("123456"),
                Set.of(Role.LEADER)));
    }


    @Test
    void getMyInfo_no_token_should_return_unauthorized() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyInfo_with_token_should_return_user_info() throws Exception {
        String token = TestJwtProvider.generateToken(
                "john",
                List.of("LEADER")
        );

        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    @Test
    void updatePassword_no_token_should_return_unauthorized(){
        try {
            mockMvc.perform(put("/api/user/password"))
                    .andExpect(status().isUnauthorized());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updatePassword_with_token_should_return_成功更新密碼() throws Exception {
        String token = TestJwtProvider.generateToken(
                "john", List.of("LEADER")
        );

        var request = new UpdatePasswordRequest(
                "123456",
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