package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIndustryService;

import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerIndustryController {

    private final IBusinessPartnerIndustryService businessPartnerIndustryService;

    @PostMapping("/businessPartnerIndustries/create")
    public ResponseEntity<BusinessPartnerIndustry> create(
            @RequestBody BusinessPartnerIndustryResource businessPartnerIndustryResource, 
            @RequestParam UUID businessPartnerId) {

        businessPartnerIndustryResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(businessPartnerIndustryService.create(businessPartnerIndustryResource));
    }

    @PutMapping("/businessPartnerIndustries/update")
    public ResponseEntity<BusinessPartnerIndustry> update(
            @RequestBody BusinessPartnerIndustryResource businessPartnerIndustryResource) {
                
        return ResponseEntity.ok(businessPartnerIndustryService.update(businessPartnerIndustryResource));
    }
}
