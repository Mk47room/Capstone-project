package com.springboot.careassist.dto.response;

import java.time.Instant;

public record ClaimInvoiceRespDto(
        Instant invoiceDate,
        double consultationFee,
        double diagnosticsFee,
        double pharmacyFee,
        double roomCharges,
        double tax,
        double totalAmount,
        Long claimId,
        String diagnosis,
        double claimedAmount,
        double approvedAmount

) {
}
