package org.kangning.church.auth.adapter.out.persistence.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.membership.adaptor.out.persistent.entity.MembershipEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 30)
    private String account;

    @Column(nullable = false, length = 200)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_global_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> globalRoles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipEntity> memberships = new ArrayList<>();

    public void addMembership(MembershipEntity membership) {
        memberships.add(membership);
        membership.setUser(this);
    }

    public void removeMembership(MembershipEntity membership) {
        memberships.remove(membership);
        membership.setUser(null);
    }
}
