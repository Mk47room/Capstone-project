package com.springboot.careassist.mapper;

import com.springboot.careassist.dto.request.InvoiceReqDto;
import com.springboot.careassist.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public static Invoice mapDtoToInvoice(InvoiceReqDto dto){
        Invoice invoice = new Invoice();

        invoice.setConsultationFee(dto.consultationFee());
        invoice.setDiagnosticsFee(dto.diagnosticsFee());
        invoice.setPharmacyFee(dto.pharmacyFee());
        invoice.setRoomCharges(dto.roomCharges());

        return  invoice;
    }

}
