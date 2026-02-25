package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerKYCDetail;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerKYCDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerKYCDetailService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerKYCDetailController {

    private final IBusinessPartnerKYCDetailService businessPartnerKYCDetailService;

    @PostMapping("/businessPartnerKYCDetails/create")
    public ResponseEntity<BusinessPartnerKYCDetail> create(
            @RequestBody BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource,
            @RequestParam UUID businessPartnerId,
            HttpServletRequest request) {

        businessPartnerKYCDetailResource.setPartnerId(businessPartnerId);
        return ResponseEntity.ok(businessPartnerKYCDetailService.create(businessPartnerKYCDetailResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/businessPartnerKYCDetails/update")
    public ResponseEntity<BusinessPartnerKYCDetail> update(
            @RequestBody BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource,
            HttpServletRequest request) throws CloneNotSupportedException {
                
        return ResponseEntity.ok(businessPartnerKYCDetailService.update(businessPartnerKYCDetailResource,
                request.getUserPrincipal().getName()));
    }
}
