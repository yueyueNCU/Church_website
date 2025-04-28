package org.kangning.church.auth.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
public class User {
    private String username;
    private String passwordHash;
    private List<Role> globalRoles;
    private List<ChurchRole> userChurchRoles;
}
