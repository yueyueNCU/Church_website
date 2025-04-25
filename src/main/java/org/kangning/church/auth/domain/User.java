package org.kangning.church.auth.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
public class User {
    private String username;
    private String passwordHash;
    private List<Role> roles;


    private boolean hasRole(Role role){
        return roles.contains(role);
    }
    public boolean isAdmin(){
        return hasRole(Role.ADMIN);
    }
    public boolean isLeader(){
        return hasRole(Role.LEADER);
    }
    public boolean isMember(){
        return hasRole(Role.MEMBER);
    }
}
