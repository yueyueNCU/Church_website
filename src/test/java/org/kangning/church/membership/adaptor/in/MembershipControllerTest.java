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
import org.kangning.church.membership.adaptor.in.dto.UpdateMembershipRoleRequest;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.kangning.church.testutil.TestJwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void getChurchMembers_withValidTokenAndProperRoleAndMatchingChurchId_shouldReturnOk() throws Exception {
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
    void getChurchMembers_withWrongChurchContext_shouldReturnForbidden() throws Exception{
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
                .andExpect(status().isForbidden());
    }




    @Test
    void getMyChurchMembership_withValidTokenAndProperRoleAndMatchingChurchId_shouldReturnOk() throws Exception {
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        mockMvc.perform(get("/api/church/{churchId}/members/my", churchIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }
    @Test
    void getMyChurchMembership_withWrongChurchContext_shouldReturnNotFound() throws  Exception{
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        mockMvc.perform(get("/api/church/{churchId}/members/my", churchIdB)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value()))
                .andExpect(status().isNotFound());
    }



    @Test
    void applyMembership_withValidTokenAndProperRoleAndMatchingChurchId_shouldReturnOk() throws Exception{
        mockMvc.perform(post("/api/church/{churchId}/members/apply", churchIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }


    @Test
    void approveMembership_withValidTokenAndProperRoleAndMatchingChurchIdAndUserId_shouldReturnOk() throws Exception{
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
                ChurchMemberStatus.PENDING)
        );
        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/approve", churchIdA,userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }
    @Test
    void approveMembership_withWrongChurchContext_shouldReturnForbidden() throws Exception{
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdB,
                userIdA,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.PENDING)
        );
        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/approve", churchIdB,userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value()))
                .andExpect(status().isForbidden());
    }


    @Test
    void rejectMembership_withValidTokenAndProperRoleAndMatchingChurchIdAndUserId_shouldReturnOk() throws Exception{
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
                ChurchMemberStatus.PENDING)
        );
        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/reject", churchIdA,userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }
    @Test
    void rejectMembership_withWrongChurchContext_shouldReturnForbidden() throws Exception{
        membershipRepository.save(new Membership(
                null,
                churchIdA,
                userId,
                Set.of(Role.LEADER),
                ChurchMemberStatus.APPROVED)
        );
        membershipRepository.save(new Membership(
                null,
                churchIdB,
                userIdA,
                Set.of(Role.MEMBER),
                ChurchMemberStatus.PENDING)
        );
        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/reject", churchIdB,userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value()))
                .andExpect(status().isForbidden());
    }




    @Test
    void updateMembershipRole_withValidTokenAndProperRoleAndMatchingChurchIdAndUserId_shouldReturnOk() throws Exception{
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
        var request = new UpdateMembershipRoleRequest(Set.of(Role.LEADER));
        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/roles", churchIdA,userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    void updateMembershipRole_withWrongChurchContext_shouldReturnForbidden() throws Exception{
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
        var request = new UpdateMembershipRoleRequest(Set.of(Role.LEADER));
        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/roles", churchIdA,userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }



    @Test
    void getIndividualMembership_withValidTokenAndProperRoleAndMatchingChurchId_shouldReturnOk() throws Exception {
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
        mockMvc.perform(get("/api/church/{churchId}/members/{userId}", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }
    @Test
    void getIndividualMembership_withWrongChurchContext_shouldReturnForbidden() throws  Exception{
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
        mockMvc.perform(get("/api/church/{churchId}/members/{userId}", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value()))
                .andExpect(status().isForbidden());
    }



    @Test
    void removeMembership_withValidTokenAndProperRoleAndMatchingChurchId_shouldReturnOk() throws Exception {
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
        mockMvc.perform(delete("/api/church/{churchId}/members/{userId}", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }
    @Test
    void removeMembership_withWrongChurchContext_shouldReturnForbidden() throws  Exception{
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
        mockMvc.perform(delete("/api/church/{churchId}/members/{userId}", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdB.value()))
                .andExpect(status().isForbidden());
    }
}
