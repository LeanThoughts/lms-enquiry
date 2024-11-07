package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerLoanContactService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerLoanContactController {

    private final IBusinessPartnerLoanContactService businessPartnerLoanContactService;

    @PostMapping("/businessPartnerLoanContacts/create")
    public ResponseEntity<BusinessPartnerLoanContact> create(
            @RequestBody BusinessPartnerLoanContactResource businessPartnerLoanContactResource,
            @RequestParam UUID businessPartnerId,
            HttpServletRequest request) {

        businessPartnerLoanContactResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(businessPartnerLoanContactService.create(businessPartnerLoanContactResource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerLoanContacts/update")
    public ResponseEntity<BusinessPartnerLoanContact> update(
            @RequestBody BusinessPartnerLoanContactResource businessPartnerLoanContactResource,
            HttpServletRequest request) throws CloneNotSupportedException {
                
        return ResponseEntity.ok(businessPartnerLoanContactService.update(businessPartnerLoanContactResource, request.getUserPrincipal().getName()));
    }


    @PutMapping("/businessPartnerLoanContacts/migrate")
    public ResponseEntity<BusinessPartnerLoanContact> migrate(
            @RequestBody BusinessPartnerLoanContactMigrationResource businessPartnerLoanContactResource,
            HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(businessPartnerLoanContactService.migrate(businessPartnerLoanContactResource, request.getUserPrincipal().getName()));
    }
}
