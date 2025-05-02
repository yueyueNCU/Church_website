package org.kangning.church.membership.application.port.in;

import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;

import java.util.List;
import java.util.Set;

public interface MembershipUseCase {
    void applyMembership(UserId userId, ChurchId churchId);

    void approveMembership(ChurchId churchId, UserId userId);

    void rejectMembership(ChurchId churchId, UserId userId);

    void updateMembershipRole(ChurchId churchId, UserId userId, Set<Role> newRoles);

    void removeMembership(ChurchId churchId, UserId userId);

    List<MemberResult> getChurchMembers(ChurchId churchId);

    MemberResult getMyMembership(ChurchId churchId,UserId userId);

}
