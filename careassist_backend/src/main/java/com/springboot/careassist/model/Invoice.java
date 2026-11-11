package com.springboot.careassist.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "invoice_date",updatable = false)
    private Instant invoiceDate;

    @Column(name = "consultation_fee")
    private double consultationFee;
    @Column(name = "diagnostics_fee")
    private double diagnosticsFee;
    @Column(name = "pharmacy_fee")
    private double pharmacyFee;
    @Column(name = "room_charges")
    private double roomCharges;
    private double tax;
    @Column(name = "total_amount")
    private double totalAmount;

    @OneToOne
    @JoinColumn(nullable = false)
    private Claim claim;

    private static final int TAX_PERCENT = 8;
    @PrePersist
    @PreUpdate
        public void calculateAmounts() {
            double subtotal = consultationFee + diagnosticsFee + pharmacyFee + roomCharges;
            this.tax = Math.round(subtotal * TAX_PERCENT) / 100.0;
            this.totalAmount = Math.round((subtotal + tax) * 100.0) / 100.0;
    }
}
