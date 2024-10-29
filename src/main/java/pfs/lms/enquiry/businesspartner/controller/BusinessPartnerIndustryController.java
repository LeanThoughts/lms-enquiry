package pfs.lms.enquiry.businesspartner.controller;

import java.util.UUID;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIndustryService;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerIndustryController {

    private final IBusinessPartnerIndustryService businessPartnerIndustryService;

    @PostMapping("/businessPartnerIndustries/create")
    public ResponseEntity<BusinessPartnerIndustry> create(
            @RequestBody BusinessPartnerIndustryResource businessPartnerIndustryResource, 
            @RequestParam UUID partnerId) {

        businessPartnerIndustryResource.setPartnerId(partnerId);
        return ResponseEntity.ok(businessPartnerIndustryService.create(businessPartnerIndustryResource));
    }

    @PutMapping("/businessPartnerIndustries/update")
    public ResponseEntity<BusinessPartnerIndustry> update(
            @RequestBody BusinessPartnerIndustryResource businessPartnerIndustryResource) {
                
        return ResponseEntity.ok(businessPartnerIndustryService.update(businessPartnerIndustryResource));
    }
}
