package org.kangning.church.membership.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.MembershipId;
import org.kangning.church.common.identifier.UserId;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Membership {
    private final MembershipId id;               // 可為 null，儲存時由 JPA 產生
    private final ChurchId churchId;
    private final UserId userId;

    private Set<ChurchRole> roles;
    private  ChurchMemberStatus status;

    public static Membership pending(ChurchId churchId, UserId userId) {
        return new Membership(null, churchId, userId,
                null, ChurchMemberStatus.PENDING);
    }

    public boolean isApproved(){
        return status == ChurchMemberStatus.APPROVED;
    }
}
