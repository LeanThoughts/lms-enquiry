package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerIdentificationController {

    private final IBusinessPartnerIdentificationService businessPartnerIdentificationService;

    @PostMapping("/businessPartnerIdentifications/create")
    public ResponseEntity<BusinessPartnerIdentification> create(
            @RequestBody BusinessPartnerIdentificationResource businessPartnerIdentificationResource,
            @RequestParam UUID businessPartnerId,
            HttpServletRequest request) {

        businessPartnerIdentificationResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(businessPartnerIdentificationService.create(businessPartnerIdentificationResource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerIdentifications/update")
    public ResponseEntity<BusinessPartnerIdentification> update(
            @RequestBody BusinessPartnerIdentificationResource businessPartnerIdentificationResource,
            HttpServletRequest request) throws CloneNotSupportedException {
                
        return ResponseEntity.ok(businessPartnerIdentificationService.update(businessPartnerIdentificationResource, request.getUserPrincipal().getName()));
    }
}
