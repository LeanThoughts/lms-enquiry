package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIndustryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerIndustryController {

    private final IBusinessPartnerIndustryService businessPartnerIndustryService;

    @PostMapping("/businessPartnerIndustries/create")
    public ResponseEntity<BusinessPartnerIndustry> create(
            @RequestBody BusinessPartnerIndustryResource businessPartnerIndustryResource,
            @RequestParam UUID businessPartnerId,
            HttpServletRequest request) {

        businessPartnerIndustryResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(businessPartnerIndustryService.create(businessPartnerIndustryResource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerIndustries/update")
    public ResponseEntity<BusinessPartnerIndustry> update(
            @RequestBody BusinessPartnerIndustryResource businessPartnerIndustryResource,
            HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(businessPartnerIndustryService.update(businessPartnerIndustryResource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerIndustries/migrate")
    public ResponseEntity<BusinessPartnerIndustry> migrate(
            @RequestBody BusinessPartnerIndustryMigrationResource businessPartnerIndustryResource,
            HttpServletRequest request) throws CloneNotSupportedException {
                
        return ResponseEntity.ok(businessPartnerIndustryService.migrate(businessPartnerIndustryResource, request.getUserPrincipal().getName()));
    }

    @GetMapping("/businessPartnerIndustries/findByPartnerId")
    public ResponseEntity<List<BusinessPartnerIndustryResource>> findByPartnerId(
            @RequestParam UUID partnerId)
    {
        return ResponseEntity.ok(businessPartnerIndustryService.findByPartnerId(partnerId));
    }
}
