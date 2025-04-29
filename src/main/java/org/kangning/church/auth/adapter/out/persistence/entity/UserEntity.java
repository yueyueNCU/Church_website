package org.kangning.church.auth.adapter.out.persistence.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 200)
    private String passwordHash;

    /** 全站權限，例如 SITE_ADMIN */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_global_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> globalRoles = new java.util.HashSet<>();
}