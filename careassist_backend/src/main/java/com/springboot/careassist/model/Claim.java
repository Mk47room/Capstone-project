package com.springboot.careassist.model;

import com.springboot.careassist.enums.ClaimStatus;
import com.springboot.careassist.enums.ClaimType;
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
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ClaimType claimType;

    @Column(nullable = false)
    private String diagnosis;

    private String treatment;

    @Column(name = "claimed_amount")
    private double claimedAmount;

    @Column(name = "admission_date",nullable = false)
    private LocalDate admissionDate;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @CreationTimestamp
    @Column(name = "submitted_date",updatable = false)
    private Instant submittedDate;

    @Column(name = "approved_amount")
    private double approvedAmount;

    @Column(name = "rejected_reason")
    private String rejectedReason;

    @Enumerated(value = EnumType.STRING)
    private ClaimStatus status;

    @ManyToOne
    @JoinColumn(name = "policy_id",nullable = false)
    private Policy policy;

    // Reimbursement field
    @Column(name = "hospital_name")
    private String hospitalName;

    // Cashless field
    @ManyToOne
    @JoinColumn(name = "health_care_provider_id")
    private HealthCareProvider healthCareProvider;

    @PrePersist
    public void init(){
        status = ClaimStatus.SUBMITTED;
    }

}
