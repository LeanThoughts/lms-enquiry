package pfs.lms.enquiry.businesspartner.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerRole;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleRepository;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerRoleTypeRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerRoleResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerRoleService;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Service
@RequiredArgsConstructor
public class BusinessPartnerRoleService implements IBusinessPartnerRoleService {

    private final BusinessPartnerRoleRepository businessPartnerRoleRepository;
    private final PartnerRepository partnerRepository;
    private final BusinessPartnerRoleTypeRepository businessPartnerRoleTypeRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BusinessPartnerRole create(BusinessPartnerRoleResource businessPartnerRoleResource, String username) {
        if (businessPartnerRoleRepository.findByPartnerIdAndRoleTypeId(businessPartnerRoleResource.getBusinessPartnerId(), 
            businessPartnerRoleResource.getRoleTypeId()) != null) {
                
            throw new RuntimeException("Business partner role already exists");
        }

        BusinessPartnerRole businessPartnerRole = new BusinessPartnerRole();
        businessPartnerRole.setPartner(partnerRepository.findById(businessPartnerRoleResource.getBusinessPartnerId()).
            orElseThrow(() -> new RuntimeException("Partner not found")));
        businessPartnerRole.setRoleType(businessPartnerRoleTypeRepository.findById(businessPartnerRoleResource.getRoleTypeId()).
            orElseThrow(() -> new RuntimeException("Role type not found")));
        businessPartnerRole.setDifferentiationType(businessPartnerRoleResource.getDifferentiationType());
        businessPartnerRole.setAllPartnerRoles(businessPartnerRoleResource.getAllPartnerRoles());
        businessPartnerRole.setValidFromDate(businessPartnerRoleResource.getValidFromDate());
        businessPartnerRole =  businessPartnerRoleRepository.save(businessPartnerRole);

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
