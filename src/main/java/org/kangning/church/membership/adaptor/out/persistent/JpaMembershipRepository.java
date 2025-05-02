package org.kangning.church.membership.adaptor.out.persistent;

import lombok.RequiredArgsConstructor;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.adaptor.out.persistent.entity.MembershipEntity;
import org.kangning.church.membership.adaptor.out.persistent.mapper.MembershipMapper;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMembershipRepository implements MembershipRepositoryPort {

    private final H2MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;

    @Override
    public Membership save(Membership membership) {
        MembershipEntity saved = membershipRepository.save(membershipMapper.toEntity(membership));
        return membershipMapper.toDomain(saved);
    }

    @Override
    public Optional<Membership> findByChurchIdAndUserId(ChurchId churchId, UserId userId) {
        return membershipRepository.findByChurchIdAndUserId(churchId.value(), userId.value()).map(membershipMapper::toDomain);
    }

    @Override
    public List<Membership> findAllByChurchId(ChurchId churchId) {
        return membershipRepository.findAllByChurchId(churchId.value()).stream()
                .map(membershipMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByChurchIdAndUserId(ChurchId churchId, UserId userId) {
        membershipRepository.deleteByChurchIdAndUserId(churchId.value(), userId.value());
    }

    @Override
    public List<Membership> findAllByUserId(UserId userId){
        return membershipRepository.findAllByUserId(userId.value()).stream()
                .map(membershipMapper::toDomain)
                .toList();
    }

    @Override
    public List<Membership> findApprovedByUserId(UserId userId) {
        return membershipRepository.findAllByUserIdAndStatus(
                        userId.value(),                       // Long
                        ChurchMemberStatus.APPROVED          // Enum
                ).stream()
                .map(membershipMapper::toDomain)
                .toList();
    }
    @Override
    public void deleteByAll() {
        membershipRepository.deleteAll();
    }
}
