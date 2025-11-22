package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerFinancial;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerFinancialResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerFinancialService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerFinancialController {

    private final IBusinessPartnerFinancialService iBusinessPartnerFinancialService;

    @PostMapping("/businessPartnerFinancials/create")
    public ResponseEntity<BusinessPartnerFinancial> create(
            @RequestBody BusinessPartnerFinancialResource businessPartnerFinancialResource,
            @RequestParam UUID businessPartnerId,
            HttpServletRequest request) {

        businessPartnerFinancialResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(iBusinessPartnerFinancialService.create(businessPartnerFinancialResource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerFinancials/update")
    public ResponseEntity<BusinessPartnerFinancial> update(
            @RequestBody BusinessPartnerFinancialResource businessPartnerFinancialResource,
            HttpServletRequest request) throws CloneNotSupportedException {
                
        return ResponseEntity.ok(iBusinessPartnerFinancialService.update(businessPartnerFinancialResource, request.getUserPrincipal().getName()));
    }
}
