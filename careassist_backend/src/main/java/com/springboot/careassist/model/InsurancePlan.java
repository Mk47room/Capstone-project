package com.springboot.careassist.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "insurance_plan")
public class InsurancePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "base_premium",nullable = false)
    private double basePremium;

    @Column(name = "coverage_amount",nullable = false)
    private double coverageAmount;

    @Column(name = "age_min")
    private int ageMin;
    @Column(name = "age_max")
    private int ageMax;
}
