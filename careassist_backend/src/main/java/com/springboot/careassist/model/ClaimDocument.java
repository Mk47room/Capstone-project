package com.springboot.careassist.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "claim_document")
public class ClaimDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "doc_type")
    private String docType;

    @ManyToOne
    @JoinColumn(name = "claim_id",nullable = false)
    private Claim claim;
}
