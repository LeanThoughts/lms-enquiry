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
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerBankDetailService;

import javax.servlet.http.HttpServletRequest;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerBankDetailController {

    private final IBusinessPartnerBankDetailService businessPartnerBankDetailService;

    @PostMapping("/businessPartnerBankDetails/create")
    public ResponseEntity<BusinessPartnerBankDetail> create(
            @RequestBody BusinessPartnerBankDetailResource businessPartnerBankDetailResource,
            @RequestParam UUID businessPartnerId,
            HttpServletRequest request) {

        businessPartnerBankDetailResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(businessPartnerBankDetailService.create(businessPartnerBankDetailResource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerBankDetails/update")
    public ResponseEntity<BusinessPartnerBankDetail> update(
            @RequestBody BusinessPartnerBankDetailResource businessPartnerBankDetailResource,
            HttpServletRequest request) throws CloneNotSupportedException {
                
        return ResponseEntity.ok(businessPartnerBankDetailService.update(businessPartnerBankDetailResource,request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerBankDetails/migrate")
    public ResponseEntity<BusinessPartnerBankDetail> migrate(
            @RequestBody BusinessPartnerBankDetailMigrationResource businessPartnerBankDetailResource,
            HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(businessPartnerBankDetailService.migrate(businessPartnerBankDetailResource,request.getUserPrincipal().getName()));
    }

}
