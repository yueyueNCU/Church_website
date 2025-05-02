package org.kangning.church.auth.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.kangning.church.common.identifier.UserId;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
public class User {
    private final UserId id;              // 允許 null，儲存時由 JPA 產生
    private  String username;
    private final String account;
    private String passwordHash;
    private Set<Role> globalRoles;
}
