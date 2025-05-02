package org.kangning.church.membership.adaptor.out.persistent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kangning.church.auth.domain.Role;
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
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 教會 ID */
    @Column(name = "church_id", nullable = false)
    private Long churchId;

    /** 教會內角色 */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "membership_roles", joinColumns = @JoinColumn(name = "membership_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    /** 成員狀態（PENDING, APPROVED） */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChurchMemberStatus status;
}
