package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRoleType;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerRoleMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerRoleResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerRoleService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerRoleService implements IBusinessPartnerRoleService {

    private final BusinessPartnerRoleRepository businessPartnerRoleRepository;
    private final PartnerRepository partnerRepository;
    private final BusinessPartnerRoleTypeRepository businessPartnerRoleTypeRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BusinessPartnerRole create(BusinessPartnerRoleResource businessPartnerRoleResource, String username) {
        if (businessPartnerRoleRepository.findByPartnerIdAndRoleTypeId(businessPartnerRoleResource.getBusinessPartnerId(), 
            businessPartnerRoleResource.getRoleTypeId()) != null) {
                
            log.info("Business partner role already exists");
        }

        BusinessPartnerRole businessPartnerRole = new BusinessPartnerRole();

        Partner partner = partnerRepository.findById(businessPartnerRoleResource.getBusinessPartnerId()).
            orElseThrow(() -> new RuntimeException("Partner not found"));
        businessPartnerRole.setPartner(partner);
        
        BusinessPartnerRoleType roleType = businessPartnerRoleTypeRepository.findById(businessPartnerRoleResource.getRoleTypeId()).
            orElseThrow(() -> new RuntimeException("Role type not found"));
        businessPartnerRole.setRoleType(roleType);
        
        businessPartnerRole.setDifferentiationType(businessPartnerRoleResource.getDifferentiationType());
        businessPartnerRole.setAllPartnerRoles(businessPartnerRoleResource.getAllPartnerRoles());
        businessPartnerRole.setValidFromDate(businessPartnerRoleResource.getValidFromDate());
        businessPartnerRole.setValidToDate(businessPartnerRoleResource.getValidToDate());
        businessPartnerRole =  businessPartnerRoleRepository.save(businessPartnerRole);
        businessPartnerRole.setCreatedAt(LocalTime.now());
        businessPartnerRole.setCreatedOn(LocalDate.now());
        businessPartnerRole.setCreatedByUserName(username);

        if (businessPartnerRoleResource.isDefaultRole()) {
            partner.setPartyRole(roleType.getCode());
        }

        partner.setWorkFlowStatusCode(11); //Updated
        partnerRepository.save(partner);

        changeDocumentService.createChangeDocument(
                businessPartnerRole.getId(),
                businessPartnerRole.getId().toString(),
                businessPartnerRole.getPartner().getId().toString(),
                businessPartnerRole.getPartner().getId().toString(),
                null,
                businessPartnerRole,
                "Created",
                username,
                "Partner", "BusinessPartnerRole");

        return businessPartnerRole;
    }


    @Override
    public BusinessPartnerRole migrate(BusinessPartnerRoleMigrationResource businessPartnerRoleResource, String username) {

        BusinessPartnerRoleType businessPartnerRoleType = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode(businessPartnerRoleResource.getRoleType());



        BusinessPartnerRole businessPartnerRole = new BusinessPartnerRole();

        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerRoleResource.getPartnerId()));
        if (partner == null) {
            log.error("Business Partner Master Data Not Found for ID: " + businessPartnerRoleResource.getPartnerId());
            return null;
        }

        if (businessPartnerRoleRepository.findByPartnerIdAndRoleTypeId(partner.getId(), businessPartnerRoleType.getId()) != null) {
            log.info("Business partner role already exists" + businessPartnerRoleResource.getRoleType());
            return null;
        }


        BusinessPartnerRoleType roleType = businessPartnerRoleTypeRepository.findBusinessPartnerRoleByCode(businessPartnerRoleResource.getRoleType());

        businessPartnerRole.setRoleType(roleType);
        businessPartnerRole.setPartner(partner);
        businessPartnerRole.setDifferentiationType(businessPartnerRoleResource.getDifferentiationType());
        businessPartnerRole.setAllPartnerRoles(businessPartnerRoleResource.getAllPartnerRoles());
        businessPartnerRole.setValidFromDate(businessPartnerRoleResource.getValidFromDate());
        businessPartnerRole.setValidToDate(businessPartnerRoleResource.getValidToDate());

        businessPartnerRole.setCreatedAt(LocalTime.now());
        businessPartnerRole.setCreatedOn(LocalDate.now());
        businessPartnerRole.setCreatedByUserName(username);

        businessPartnerRole =  businessPartnerRoleRepository.save(businessPartnerRole);

        if (businessPartnerRoleResource.getDefaultRole().length() > 0) {
            partner.setPartyRole(roleType.getCode());
            partner = partnerRepository.save(partner);
        }

        changeDocumentService.createChangeDocument(
                businessPartnerRole.getId(),
                businessPartnerRole.getId().toString(),
                businessPartnerRole.getPartner().getId().toString(),
                businessPartnerRole.getPartner().getId().toString(),
                null,
                businessPartnerRole,
                "Created",
                username,
                "Partner", "BusinessPartnerRole");

        return businessPartnerRole;
    }




}
