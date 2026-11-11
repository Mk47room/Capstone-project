package com.springboot.careassist.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "health_care_provider")
public class HealthCareProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

}
