package com.springboot.careassist.model;

import com.springboot.careassist.enums.Gender;
import com.springboot.careassist.enums.RiskCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dob;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String contact;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "risk_category")
    private RiskCategory riskCategory;

    @Column(name = "chronic_illness")
    private String chronicIllness;
    @Column(name = "pre_existing")
    private String preExisting;
    @Column(name = "acute_symptoms")
    private String acuteSymptoms;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Instant updateAt;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @PrePersist
    public void init(){
        if(riskCategory == null)
            riskCategory = RiskCategory.NONE;
    }
}
