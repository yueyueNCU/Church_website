package org.kangning.church.membership.adaptor.out.persistent;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.out.persistence.H2UserRepository;
import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.church.adapter.out.persistent.H2ChurchRepository;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.churchRole.adapter.out.H2ChurchRoleRepository;
import org.kangning.church.churchRole.adapter.out.entity.ChurchRoleEntity;
import org.kangning.church.churchRole.adapter.out.mapper.ChurchRoleMapper;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.kangning.church.common.exception.church.ChurchNotFoundException;
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
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaMembershipRepository implements MembershipRepositoryPort {

    private final H2MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;

    private final H2ChurchRepository churchRepository;
    private final H2UserRepository userRepository;

    private final H2ChurchRoleRepository churchRoleRepository;
    private final ChurchRoleMapper roleMapper;

    @Override
    public Membership save(Membership domain) {
        MembershipEntity entity = membershipMapper.toEntity(domain);
        // 在這裡查詢關聯
        ChurchEntity church = churchRepository.findById(domain.getChurchId().value())
                .orElseThrow(ChurchNotFoundException::new);
        UserEntity user = userRepository.findById(domain.getUserId().value())
                .orElseThrow(UserNotFoundException::new);

        entity.setChurch(church);
        entity.setUser(user);

        if(domain.getRoles() != null) {
            Set<ChurchRoleEntity> roleEntities = domain.getRoles().stream()
                    .map(role -> churchRoleRepository.findById(role.getId().value())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role.getId())))
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }

        return membershipMapper.toDomain(membershipRepository.save(entity));
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
