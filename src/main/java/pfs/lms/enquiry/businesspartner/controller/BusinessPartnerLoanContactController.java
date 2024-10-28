package pfs.lms.enquiry.businesspartner.controller;

import java.util.UUID;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerLoanContactService;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerLoanContactController {

    private final IBusinessPartnerLoanContactService businessPartnerLoanContactService;

    @PostMapping("/businessPartnerLoanContacts/create")
    public ResponseEntity<BusinessPartnerLoanContact> create(
            @RequestBody BusinessPartnerLoanContactResource businessPartnerLoanContactResource, 
            @RequestParam UUID partnerId) {

        businessPartnerLoanContactResource.setPartnerId(partnerId);
        return ResponseEntity.ok(businessPartnerLoanContactService.create(businessPartnerLoanContactResource));
    }

    @PutMapping("/businessPartnerLoanContacts/update")
    public ResponseEntity<BusinessPartnerLoanContact> update(
            @RequestBody BusinessPartnerLoanContactResource businessPartnerLoanContactResource) {
                
        return ResponseEntity.ok(businessPartnerLoanContactService.update(businessPartnerLoanContactResource));
    }
}
