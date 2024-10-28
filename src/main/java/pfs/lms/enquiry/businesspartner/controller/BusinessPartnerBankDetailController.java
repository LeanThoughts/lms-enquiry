package pfs.lms.enquiry.businesspartner.controller;

import java.util.UUID;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerBankDetailService;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerBankDetailController {

    private final IBusinessPartnerBankDetailService businessPartnerBankDetailService;

    @PostMapping("/businessPartnerBankDetails/create")
    public ResponseEntity<BusinessPartnerBankDetail> create(
            @RequestBody BusinessPartnerBankDetailResource businessPartnerBankDetailResource, 
            @RequestParam UUID partnerId) {

        businessPartnerBankDetailResource.setPartnerId(partnerId);
        return ResponseEntity.ok(businessPartnerBankDetailService.create(businessPartnerBankDetailResource));
    }

    @PutMapping("/businessPartnerBankDetails/update")
    public ResponseEntity<BusinessPartnerBankDetail> update(
            @RequestBody BusinessPartnerBankDetailResource businessPartnerBankDetailResource) {
                
        return ResponseEntity.ok(businessPartnerBankDetailService.update(businessPartnerBankDetailResource));
    }
}
