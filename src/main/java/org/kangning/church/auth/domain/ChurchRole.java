package org.kangning.church.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kangning.church.membership.domain.ChurchMemberStatus;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ChurchRole {
    private Long ChurchId;
    private List<Role> roles;
    private ChurchMemberStatus status;
}
