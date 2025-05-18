package org.kangning.church.church.adapter.out.persistent.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kangning.church.churchRole.adapter.out.entity.ChurchRoleEntity;
import org.kangning.church.membership.adaptor.out.persistent.entity.MembershipEntity;
import org.kangning.church.membership.domain.Membership;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "church")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChurchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    private String address;
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /** 與 ChurchRoleEntity 建立雙向關聯 */
    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ChurchRoleEntity> churchRoles = new HashSet<>();

    /** 與 MembershipEntity 建立雙向關聯 */
    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MembershipEntity> memberships = new ArrayList<>();

    /** JPA 生命週期回呼：插入前自動設時間 */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public void addRole(ChurchRoleEntity role) {
        churchRoles.add(role);
        role.setChurch(this);
    }

    public void removeRole(ChurchRoleEntity role) {
        churchRoles.remove(role);
        role.setChurch(null);
    }

    public void addMembership(MembershipEntity membership) {
        memberships.add(membership);
        membership.setChurch(this);
    }

    public void removeMembership(MembershipEntity membership) {
        memberships.remove(membership);
        membership.setChurch(null);
    }
}