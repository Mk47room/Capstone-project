package com.springboot.careassist.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "starting_date",nullable = false)
    private LocalDate startingDate;

    @Column(name = "ending_date",nullable = false)
    private LocalDate endingDate;

    @Column(name = "premium_amount")
    private double premiumAmount;

    @Column(name = "coverage_amount")
    private double coverageAmount;

    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "insurance_plan_id",nullable = false)
    private InsurancePlan insurancePlan;

}
