package com.springboot.careassist.controller;

import com.springboot.careassist.dto.request.InvoiceReqDto;
import com.springboot.careassist.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/api/invoice/add/{claimId}")
    public void insertInvoice(Principal principal,
                              @PathVariable Long claimId,
                              @Valid @RequestBody InvoiceReqDto dto){
        invoiceService.insertInvoice(principal.getName(),claimId,dto);
    }

}
