package org.kangning.church.membership.adaptor.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.churchRole.application.port.out.ChurchRoleRepositoryPort;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.churchRole.domain.Permission;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
class MembershipControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ChurchRepositoryPort churchRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepositoryPort userRepository;
    @Autowired private MembershipRepositoryPort membershipRepository;
    @Autowired private ChurchRoleRepositoryPort churchRoleRepository;
    private String jwtToken;
    private ChurchId churchIdA;
    private ChurchId churchIdB;
    private UserId userId;
    private UserId userIdA;
    private UserId userIdB;

    @BeforeEach
    void setup() {
        churchRepository.deleteByAll();
        userRepository.deleteByAll();
        membershipRepository.deleteByAll();

        User savedUser = userRepository.save(new User(null, "john", "TestAccount", passwordEncoder.encode("12345678"), Set.of()));
        userId = savedUser.getId();

        jwtToken = TestJwtProvider.generateToken(userId, savedUser.getUsername(), Set.of());

        userIdA = userRepository.save(new User(null, "TestUserA", "TestAccountA", passwordEncoder.encode("12345678"), Set.of())).getId();
        userIdB = userRepository.save(new User(null, "TestUserB", "TestAccountB", passwordEncoder.encode("12345678"), Set.of())).getId();

        churchIdA = churchRepository.save(new Church(null, "教會A", "地址A", "描述A", null)).getId();
        churchIdB = churchRepository.save(new Church(null, "教會B", "地址B", "描述B", null)).getId();
    }

    @Test
    void applyMembership_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/church/{churchId}/members/apply", churchIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }

    @Test
    void approveMembership_shouldReturnOk() throws Exception {
        ChurchRole leaderRole = new ChurchRole(
                null,
                churchIdA,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );
        ChurchRole savedRoles = churchRoleRepository.save(leaderRole);
        membershipRepository.save(new Membership(null, churchIdA, userId, Set.of(savedRoles), ChurchMemberStatus.APPROVED));
        membershipRepository.save(new Membership(null, churchIdA, userIdA, Set.of(), ChurchMemberStatus.PENDING));

        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/approve", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }

    @Test
    void rejectMembership_shouldReturnOk() throws Exception {
        ChurchRole leaderRole = new ChurchRole(
                null,
                churchIdA,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );
        ChurchRole savedRoles = churchRoleRepository.save(leaderRole);
        membershipRepository.save(new Membership(null, churchIdA, userId, Set.of(savedRoles), ChurchMemberStatus.APPROVED));
        membershipRepository.save(new Membership(null, churchIdA, userIdA, Set.of(), ChurchMemberStatus.PENDING));

        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/reject", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }

    @Test
    void getMyChurchMembership_shouldReturnOk() throws Exception {
        membershipRepository.save(new Membership(null, churchIdA, userId, Set.of(), ChurchMemberStatus.APPROVED));

        mockMvc.perform(get("/api/church/{churchId}/members/my", churchIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }

    @Test
    void updateMembershipRole_shouldReturnOk() throws Exception {
        ChurchRole leaderRole = new ChurchRole(
                null,
                churchIdA,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );
        ChurchRole savedRoles = churchRoleRepository.save(leaderRole);
        membershipRepository.save(new Membership(null, churchIdA, userId, Set.of(savedRoles), ChurchMemberStatus.APPROVED));
        membershipRepository.save(new Membership(null, churchIdA, userIdA, Set.of(), ChurchMemberStatus.APPROVED));

        UpdateMembershipRoleRequest request = new UpdateMembershipRoleRequest(Set.of());

        mockMvc.perform(patch("/api/church/{churchId}/members/{userId}/roles", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getIndividualMembership_shouldReturnOk() throws Exception {
        ChurchRole leaderRole = new ChurchRole(
                null,
                churchIdA,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );
        ChurchRole savedRoles = churchRoleRepository.save(leaderRole);
        membershipRepository.save(new Membership(null, churchIdA, userId, Set.of(savedRoles), ChurchMemberStatus.APPROVED));
        membershipRepository.save(new Membership(null, churchIdA, userIdA, Set.of(), ChurchMemberStatus.APPROVED));

        mockMvc.perform(get("/api/church/{churchId}/members/{userId}", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }

    @Test
    void removeMembership_shouldReturnOk() throws Exception {
        ChurchRole leaderRole = new ChurchRole(
                null,
                churchIdA,
                "領袖",
                true,
                Set.of(Permission.ALL_PERMISSION)
        );
        ChurchRole savedRoles = churchRoleRepository.save(leaderRole);
        membershipRepository.save(new Membership(null, churchIdA, userId, Set.of(savedRoles), ChurchMemberStatus.APPROVED));
        membershipRepository.save(new Membership(null, churchIdA, userIdA, Set.of(), ChurchMemberStatus.APPROVED));

        mockMvc.perform(delete("/api/church/{churchId}/members/{userId}", churchIdA, userIdA)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Church-Id", churchIdA.value()))
                .andExpect(status().isOk());
    }
}
