package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerLoanContactRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerLoanContactService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BusinessPartnerLoanContactService implements IBusinessPartnerLoanContactService {

    private final BusinessPartnerLoanContactRepository businessPartnerLoanContactRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BusinessPartnerLoanContact create(BusinessPartnerLoanContactResource businessPartnerLoanContactResource, String username) {
        
        Partner partner = partnerRepository.findById(businessPartnerLoanContactResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerLoanContactResource.getPartnerId().toString() + " : Partner not found"));
        
        BusinessPartnerLoanContact businessPartnerLoanContact = new BusinessPartnerLoanContact();
        businessPartnerLoanContact.setPartner(partner);
        Integer lastSerialNumber = businessPartnerLoanContactRepository.findFirstByPartnerOrderBySerialNumberDesc(partner)
                .map(BusinessPartnerLoanContact::getSerialNumber)
                .orElse(0);
        businessPartnerLoanContact.setSerialNumber(lastSerialNumber + 1);
        businessPartnerLoanContact.setSelection(businessPartnerLoanContactResource.getSelection());
        businessPartnerLoanContact.setLoanNumber(businessPartnerLoanContactResource.getLoanNumber());
        businessPartnerLoanContact.setName(businessPartnerLoanContactResource.getName());
        businessPartnerLoanContact.setBranchAddress(businessPartnerLoanContactResource.getBranchAddress());
        businessPartnerLoanContact.setDesignation(businessPartnerLoanContactResource.getDesignation());
        businessPartnerLoanContact.setDepartment(businessPartnerLoanContactResource.getDepartment());
        businessPartnerLoanContact.setTelephoneNumber(businessPartnerLoanContactResource.getTelephoneNumber());
        businessPartnerLoanContact.setLandLineNumber(businessPartnerLoanContactResource.getLandLineNumber());
        businessPartnerLoanContact.setEmail(businessPartnerLoanContactResource.getEmail());
        businessPartnerLoanContact.setFaxNumber(businessPartnerLoanContactResource.getFaxNumber());
        businessPartnerLoanContact = businessPartnerLoanContactRepository.save(businessPartnerLoanContact);

        changeDocumentService.createChangeDocument(
                businessPartnerLoanContact.getId(),
                businessPartnerLoanContact.getId().toString(),
                businessPartnerLoanContact.getPartner().getId().toString(),
                businessPartnerLoanContact.getPartner().getId().toString(),
                null,
                businessPartnerLoanContact,
                "Created",
                username,
                "Partner", "BusinessPartnerLoanContact");

        return businessPartnerLoanContact;
    }

    @Override
    public BusinessPartnerLoanContact update(BusinessPartnerLoanContactResource businessPartnerLoanContactResource, String username) throws CloneNotSupportedException {
        BusinessPartnerLoanContact businessPartnerLoanContact = businessPartnerLoanContactRepository.findById(businessPartnerLoanContactResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerLoanContactResource.getId().toString() + " : Business Partner Loan Contact not found"));

        Object oldObject = businessPartnerLoanContact.clone();

        businessPartnerLoanContact.setSelection(businessPartnerLoanContactResource.getSelection());
        businessPartnerLoanContact.setLoanNumber(businessPartnerLoanContactResource.getLoanNumber());
        businessPartnerLoanContact.setName(businessPartnerLoanContactResource.getName());
        businessPartnerLoanContact.setBranchAddress(businessPartnerLoanContactResource.getBranchAddress());
        businessPartnerLoanContact.setDesignation(businessPartnerLoanContactResource.getDesignation());
        businessPartnerLoanContact.setDepartment(businessPartnerLoanContactResource.getDepartment());
        businessPartnerLoanContact.setTelephoneNumber(businessPartnerLoanContactResource.getTelephoneNumber());
        businessPartnerLoanContact.setLandLineNumber(businessPartnerLoanContactResource.getLandLineNumber());
        businessPartnerLoanContact.setEmail(businessPartnerLoanContactResource.getEmail());
        businessPartnerLoanContact.setFaxNumber(businessPartnerLoanContactResource.getFaxNumber());

        changeDocumentService.createChangeDocument(
                businessPartnerLoanContact.getId(),
                businessPartnerLoanContact.getId().toString(),
                businessPartnerLoanContact.getPartner().getId().toString(),
                businessPartnerLoanContact.getPartner().getId().toString(),
                oldObject,
                businessPartnerLoanContact,
                "Updated",
                username,
                "Partner", "BusinessPartnerLoanContact");

        return businessPartnerLoanContact;
    }
    
}
