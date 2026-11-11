package com.springboot.careassist.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record InvoiceReqDto(
        @PositiveOrZero(message = "Consultation fee cannot be negative")
        double consultationFee,
        @PositiveOrZero(message = "Diagnostics fee cannot be negative")
        double diagnosticsFee,
        @PositiveOrZero(message = "Pharmacy fee cannot be negative")
        double pharmacyFee,
        @PositiveOrZero(message = "Room Charges cannot be negative")
        double roomCharges
) {
}
