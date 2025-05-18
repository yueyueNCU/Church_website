package org.kangning.church.churchRole.adapter.out.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.churchRole.domain.Permission;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "churchRole")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ChurchRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isDefault;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "church_id", nullable = false)
    private ChurchEntity church;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "church_role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();
}