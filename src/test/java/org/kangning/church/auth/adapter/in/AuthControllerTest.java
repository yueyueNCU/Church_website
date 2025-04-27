package org.kangning.church.auth.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.application.port.in.login.dto.LoginRequest;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryPort userRepositoryPort; // ✅ 注入你的 User Repository

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ 注入 PasswordEncoder

    @BeforeEach
    void resetUserPassword() {
        userRepositoryPort.findByUsername("john").ifPresent(user -> {
            user.setPasswordHash(passwordEncoder.encode("123456"));
            userRepositoryPort.save(user);
        });
    }

    @Test
    void login_成功回傳JWTToken() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("john", "123456");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()); // 預期回傳一個 token
    }
}
