package org.kangning.church.membership.adaptor.out.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.churchRole.adapter.out.entity.ChurchRoleEntity;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.membership.domain.ChurchMemberStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "membership", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "church_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 使用者 ID */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private ChurchEntity church;

    /** 教會內角色 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "membership_roles",
            joinColumns = @JoinColumn(name = "membership_id"),
            inverseJoinColumns = @JoinColumn(name = "church_role_id"))
    private Set<ChurchRoleEntity> roles = new HashSet<>();

    /** 成員狀態（PENDING, APPROVED） */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChurchMemberStatus status;
}
