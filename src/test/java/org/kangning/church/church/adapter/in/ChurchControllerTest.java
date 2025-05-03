package org.kangning.church.church.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.adapter.in.dto.CreateChurchRequest;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.application.service.MembershipService;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.kangning.church.testutil.TestJwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private String jwtToken;
    @Autowired
    private MembershipRepositoryPort membershipRepository;

    private ChurchId churchId;
    private UserId userId;
    @BeforeEach
    void setup() {
        // 清空資料
        churchRepository.deleteByAll();
        userRepository.deleteByAll();

        // 建立測試 user
        User savedUser = userRepository.save(new User(
                null,
                "john",
                "TestAccount",
                passwordEncoder.encode("12345678"),
                Set.of(Role.LEADER))
        );
        userId=savedUser.getId();
        Church savedChurch = churchRepository.save(new Church(
                null,
                "測試教會",
                "台北市123",
                "建立於2005年",
                null)
        );
        churchId = savedChurch.getId();

        jwtToken = TestJwtProvider.generateToken(
                savedUser.getId(),
                savedUser.getUsername(),
                Set.of(Role.LEADER)
        );

        membershipRepository.save(new Membership(
                null,
                churchId,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );


    }
    @Test
    void createChurch_withValidTokenAndRequest_shouldReturnCreated() throws Exception {

        CreateChurchRequest request=new CreateChurchRequest("康寧街教會","康寧街141巷5號","建立於19xx年");

        mockMvc.perform(post("/api/church")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
    @Test
    void createChurch_withValidTokenButInvalidRequest_shouldReturnConflict() throws Exception {

        CreateChurchRequest request=new CreateChurchRequest("測試教會","康寧街141","建立於20xx年");

        mockMvc.perform(post("/api/church")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getMyChurches_withValidToken_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/church/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void searchChurch_withValidToken_shouldReturnOk() throws Exception {

        mockMvc.perform(get("/api/church/search")
                        .param("keyword", "測")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}