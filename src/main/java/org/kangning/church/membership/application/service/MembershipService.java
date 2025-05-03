package org.kangning.church.membership.application.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.membership.ChurchApplicantNotExistException;
import org.kangning.church.common.exception.membership.MembershipAlreadyExistsException;
import org.kangning.church.common.exception.membership.MembershipNotFoundException;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.in.MemberResult;
import org.kangning.church.membership.application.port.in.MembershipUseCase;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.Membership;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class MembershipService implements MembershipUseCase {

    private final MembershipRepositoryPort membershipRepository;

    private final UserRepositoryPort userRepository;

    @Override
    public void applyMembership(UserId userId, ChurchId churchId) {
        Optional<Membership> membership = membershipRepository.findByChurchIdAndUserId(churchId, userId);
        if (membership.isPresent()) throw new MembershipAlreadyExistsException();
        membershipRepository.save(Membership.pending(churchId, userId));
    }

    @Override
    public void approveMembership(ChurchId churchId, UserId userId) {
        Membership membership = membershipRepository
                .findByChurchIdAndUserId(churchId, userId)
                .orElseThrow(ChurchApplicantNotExistException::new);

        if (membership.isApproved()) throw new MembershipAlreadyExistsException();

        membership.approve(List.of(Role.MEMBER));
        membershipRepository.save(membership);


    }

    @Override
    public void rejectMembership(ChurchId churchId, UserId userId) {
        Membership membership = membershipRepository
                .findByChurchIdAndUserId(churchId, userId)
                .orElseThrow(ChurchApplicantNotExistException::new);

        if (membership.isApproved()) throw new MembershipAlreadyExistsException();

        membershipRepository.deleteByChurchIdAndUserId(churchId, userId);
    }

    @Override
    public void updateMembershipRole(ChurchId churchId, UserId userId, Set<Role> newRoles) {
        Membership membership = membershipRepository
                .findByChurchIdAndUserId(churchId, userId)
                .orElseThrow(ChurchApplicantNotExistException::new);

        if (!membership.isApproved()) throw new IllegalStateException("未核准會員不得調整角色");

        membership.approve(List.copyOf(newRoles));
        membershipRepository.save(membership);
    }

    @Override
    public void removeMembership(ChurchId churchId, UserId userId) {
        Membership membership = membershipRepository
                .findByChurchIdAndUserId(churchId, userId)
                .orElseThrow(ChurchApplicantNotExistException::new);

        membershipRepository.deleteByChurchIdAndUserId(churchId, userId);
    }

    @Override
    public List<MemberResult> getChurchMembers(ChurchId churchId) {
        List<Membership> memberships = membershipRepository.findAllByChurchId(churchId);

        return memberships.stream()
                .filter(Membership::isApproved)
                .map(m -> {
                    String username = userRepository.findById(m.getUserId())
                            .map(User::getUsername)
                            .orElse("unknown"); // 可加例外處理

                    return new MemberResult(
                            m.getUserId(),
                            username,
                            m.getRoles(),
                            m.getStatus()
                    );
                })
                .toList();
    }

    @Override
    public MemberResult getMyMembership(ChurchId churchId, UserId userId) {
        Membership membership = membershipRepository
                .findByChurchIdAndUserId(churchId, userId)
                .orElseThrow(MembershipNotFoundException::new);

        String username = userRepository.findById(membership.getUserId())
                .map(User::getUsername)
                .orElse("unknown"); // 可加例外處理

        return new MemberResult(membership.getUserId(), username, membership.getRoles(), membership.getStatus());
    }
}
