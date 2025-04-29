package org.kangning.church.church.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.adapter.in.dto.CreateChurchRequest;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.testutil.TestJwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
class ChurchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChurchRepositoryPort churchRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepositoryPort userRepository;

    @BeforeEach
    void setUp(){
        churchRepository.deleteByAll();
        userRepository.deleteByAll();
    }
    @Test
    void createChurch_withValidTokenAndProperRole_shouldReturn201() throws Exception {
        // 1. 先建一個使用者
        User savedUser = userRepository.save(new User(
                null,
                "testuser",
                passwordEncoder.encode("password123"),
                Set.of(Role.SITE_ADMIN)       // 注意權限，需要能通過Controller的 @PreAuthorize
        ));
        // 2. 產生一個 token
        String token = TestJwtProvider.generateToken(
                savedUser.getUsername(),
                List.of("ROLE_SITE_ADMIN")      // 必須帶ROLE_
        );
        // 3. 建立 request body
        CreateChurchRequest request=new CreateChurchRequest("康寧街教會","康寧街141巷5號","建立於19xx年");

        // 4. 呼叫 MockMvc，帶上 Authorization header
        mockMvc.perform(post("/api/church")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());   // 根據 REST 風格，create通常是 201 Created
    }

    @Test
    void join_withValidTokenAndProperRole_shouldReturn202() throws Exception{
        // 1. 先準備一個 User，插進資料庫
        User savedUser = userRepository.save(new User(
                null,
                "testuser",
                passwordEncoder.encode("password123"),
                Set.of(Role.MEMBER)      // 可放空或 MEMBER
        ));

        // 2. 再準備一個 Church，插進資料庫
        Church savedChurch = churchRepository.save(
                new Church(
                        null,
                        "康寧街教會",
                        "康寧街141巷5號",
                        "建立於19xx年",
                        null
                )
        );

        // 3. 生成一個合法的 JWT Token
        String token = TestJwtProvider.generateToken(
                savedUser.getUsername(),
                List.of("ROLE_MEMBER")   // 注意要加 "ROLE_"
        );

        // 4. 呼叫 /api/church/{id}/join
        mockMvc.perform(post("/api/church/" + savedChurch.getId().value() + "/join")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted());      // joinChurch() 是 202 Accepted

    }

    @Test
    void myChurches() {

    }

    @Test
    void search() {
    }
}