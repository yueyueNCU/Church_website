package org.kangning.church.church.adapter.out.persistent.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

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

    /** JPA 生命週期回呼：插入前自動設時間 */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }
}