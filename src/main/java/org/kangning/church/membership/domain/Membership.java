package org.kangning.church.membership.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.MembershipId;
import org.kangning.church.common.identifier.UserId;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Membership {
    private final MembershipId id;               // 可為 null，儲存時由 JPA 產生
    private final ChurchId churchId;
    private final UserId userId;

    private Set<Role> roles;
    private  ChurchMemberStatus status;

    public static Membership pending(ChurchId churchId, UserId userId) {
        return new Membership(null, churchId, userId,
                Set.of(Role.MEMBER), ChurchMemberStatus.PENDING);
    }

    /* ===== 行為 ===== */
    public void approve(List<Role> roles) {
        this.status = ChurchMemberStatus.APPROVED;
        this.roles  = Set.copyOf(roles);
    }
    public boolean isApproved(){
        return status == ChurchMemberStatus.APPROVED;
    }
}
