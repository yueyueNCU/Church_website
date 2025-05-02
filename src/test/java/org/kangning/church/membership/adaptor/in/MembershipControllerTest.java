package org.kangning.church.membership.adaptor.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.kangning.church.testutil.TestJwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
class MembershipControllerTest {
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

    @Autowired
    private MembershipRepositoryPort membershipRepository;

    private String jwtToken;
    private ChurchId churchIdA;
    private ChurchId churchIdB;
    private ChurchId churchIdC;
    private UserId userId;
    private UserId userIdA;
    private UserId userIdB;
    @BeforeEach
    void setup() {
        // 清空資料
        churchRepository.deleteByAll();
        userRepository.deleteByAll();
        membershipRepository.deleteByAll();
        // 建立測試 使用者 user
        User savedUser = userRepository.save(new User(
                null,
                "john",
                "TestAccount",
                passwordEncoder.encode("12345678"),
                null)
        );
        userId=savedUser.getId();

        // 建立測試 Membership
        jwtToken = TestJwtProvider.generateToken(
                savedUser.getId(),
                savedUser.getUsername(),
                Set.of(Role.LEADER)
        );

        // 建立其他 user
        User savedUserA = userRepository.save(new User(
                null,
                "TestUserA",
                "TestAccountA",
                passwordEncoder.encode("12345678"),
                null)
        );
        userIdA=savedUserA.getId();

        User savedUserB = userRepository.save(new User(
                null,
                "TestUserB",
                "TestAccountB",
                passwordEncoder.encode("12345678"),
                null)
        );
        userIdB=savedUserB.getId();



        // 建立測試教會
        Church savedChurchA = churchRepository.save(new Church(
                null,
                "測試教會A",
                "台北市 A",
                "建立於2005年 A",
                null)
        );
        churchIdA = savedChurchA.getId();

        Church savedChurchB = churchRepository.save(new Church(
                null,
                "測試教會 B",
                "台北市123 B",
                "建立於2005年 B",
                null)
        );
        churchIdB = savedChurchB.getId();
        Church savedChurchC = churchRepository.save(new Church(
                null,
                "測試教會 C",
                "台北市123 C",
                "建立於2005 C",
                null)
        );
        churchIdC = savedChurchC.getId();


    }

    @Test
    void getChurchMembers_should_return_member_list_when_user_is_leader_of_target_church() throws Exception {
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userIdA,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userIdB,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.APPROVED)
        );

        mockMvc.perform(get("/api/church/{churchId}/members", churchIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
    @Test
    void getChurchMembers_should_return_403_when_churchId_in_path_does_not_match_header() throws Exception{
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userIdA,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userIdB,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.APPROVED)
        );

        mockMvc.perform(get("/api/church/{churchId}/members", churchIdB)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    void getChurchMembers_should_return_403_when_user_is_not_member_of_target_church() throws Exception{
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userIdA,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userIdB,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.APPROVED)
        );

        mockMvc.perform(get("/api/church/{churchId}/members", churchIdB)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
