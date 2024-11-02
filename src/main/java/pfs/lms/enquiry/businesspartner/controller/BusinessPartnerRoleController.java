package pfs.lms.enquiry.businesspartner.controller;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerRoleResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerRoleService;

import javax.servlet.http.HttpServletRequest;

@RepositoryRestController
@RequiredArgsConstructor
public class BusinessPartnerRoleController {

    private final IBusinessPartnerRoleService businessPartnerRoleService;

    @PostMapping("/businessPartnerRoles/create")
    public ResponseEntity<BusinessPartnerRole> create(@RequestBody BusinessPartnerRoleResource businessPartnerRoleResource, HttpServletRequest request) {

        return ResponseEntity.ok(businessPartnerRoleService.create(businessPartnerRoleResource, request.getUserPrincipal().getName()));
    }
}
