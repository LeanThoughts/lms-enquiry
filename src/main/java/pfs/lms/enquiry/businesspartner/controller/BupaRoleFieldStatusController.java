package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingCodeRepository;
import pfs.lms.enquiry.businesspartner.resource.BupaRoleFieldStatusResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBupaRoleFieldStatusService;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerBankDetailService;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BupaRoleFieldStatusController {

    private final IBupaRoleFieldStatusService bupaRoleFieldStatusService;

     @GetMapping("/api/bupaRoleFieldStatus/{businessPartnerRole}")
    public ResponseEntity<BupaRoleFieldStatusResource> getFieldStatusByRole(@PathVariable String businessPartnerRole) {

         BupaRoleFieldStatusResource bupaRoleFieldStatusResource = bupaRoleFieldStatusService.getFieldStatusByBupaRole(businessPartnerRole);


        return ResponseEntity.ok(bupaRoleFieldStatusResource);
    }

}
