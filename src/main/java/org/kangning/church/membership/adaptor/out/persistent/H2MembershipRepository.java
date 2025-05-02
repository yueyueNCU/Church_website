package org.kangning.church.membership.adaptor.out.persistent;

import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.membership.adaptor.out.persistent.entity.MembershipEntity;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.membership.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface H2MembershipRepository extends JpaRepository<MembershipEntity,Long> {
    List<MembershipEntity> findAllByChurchId(Long id);
    Optional<MembershipEntity> findByChurchIdAndUserId(Long churchId, Long userId);
    void deleteByChurchIdAndUserId(Long churchId, Long userId);
    List<MembershipEntity> findAllByUserId(Long userId);
    List<MembershipEntity> findAllByUserIdAndStatus(Long userId, ChurchMemberStatus status);
    void deleteAll();
}
