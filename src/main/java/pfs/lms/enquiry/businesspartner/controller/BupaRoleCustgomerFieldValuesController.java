package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleCustomerFieldValues;
import pfs.lms.enquiry.businesspartner.repository.BupaRoleCustomerFieldValuesRepository;
import pfs.lms.enquiry.businesspartner.resource.BupaRoleFieldStatusResource;
import pfs.lms.enquiry.businesspartner.service.IBupaRoleFieldStatusService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BupaRoleCustgomerFieldValuesController {

    private final BupaRoleCustomerFieldValuesRepository bupaRoleCustomerFieldValuesRepository;

     @GetMapping("/buparolecustomerfieldvalues/{businessPartnerRole}")
    public ResponseEntity<BupaRoleCustomerFieldValues> getFieldStatusByRole(@PathVariable String businessPartnerRole) {

         BupaRoleCustomerFieldValues bupaRoleCustomerFieldValues = bupaRoleCustomerFieldValuesRepository.findByBupaRoleCode(businessPartnerRole);


        return ResponseEntity.ok(bupaRoleCustomerFieldValues);
    }

}
