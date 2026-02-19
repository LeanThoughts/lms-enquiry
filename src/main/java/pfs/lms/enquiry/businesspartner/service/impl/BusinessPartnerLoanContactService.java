package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerLoanContactRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerLoanContactResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerLoanContactService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        businessPartnerLoanContact.setCreatedAt(LocalTime.now());
        businessPartnerLoanContact.setCreatedOn(LocalDate.now());
        businessPartnerLoanContact.setCreatedByUserName(username);

        partner.setWorkFlowStatusCode(11); //Updated
        partnerRepository.save(partner);

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
        businessPartnerLoanContact.setChangedAt(LocalTime.now());
        businessPartnerLoanContact.setChangedOn(LocalDate.now());
        businessPartnerLoanContact.setChangedByUserName(username);
        businessPartnerLoanContact = businessPartnerLoanContactRepository.save(businessPartnerLoanContact);

        Partner partner = businessPartnerLoanContact.getPartner();
        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);
        partnerRepository.save(partner);

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


    @Override
    public BusinessPartnerLoanContact migrate(BusinessPartnerLoanContactMigrationResource businessPartnerLoanContactResource, String username) throws CloneNotSupportedException {

        Boolean update = false;
        Object oldObject = new Object();
        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerLoanContactResource.getPartnerId()));

        BusinessPartnerLoanContact businessPartnerLoanContact = new BusinessPartnerLoanContact();

        if (partner != null) {
            List<BusinessPartnerLoanContact> businessPartnerLoanContacts =
                    businessPartnerLoanContactRepository.findByLoanNumber(businessPartnerLoanContactResource.getLoanNumber());
            if (businessPartnerLoanContacts.size() > 0 ){
                businessPartnerLoanContact = businessPartnerLoanContacts.get(0);
                oldObject = businessPartnerLoanContact.clone();
                update = true;
            } else {
                businessPartnerLoanContact = new BusinessPartnerLoanContact();
            }
        } else{
            log.error("Business Partner Master Data Not Found for ID: " + businessPartnerLoanContactResource.getPartnerId());
            return null;
        }

        if (update == false){
            List<BusinessPartnerLoanContact> businessPartnerLoanContacts = businessPartnerLoanContactRepository.findByPartnerIdOrderBySerialNumberDesc(partner.getId());
            businessPartnerLoanContact.setSerialNumber(businessPartnerLoanContacts.size() + 1);
        }

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
        businessPartnerLoanContact.setPartner(partner);
        businessPartnerLoanContact.setCreatedAt(LocalTime.now());
        businessPartnerLoanContact.setCreatedOn(LocalDate.now());
        businessPartnerLoanContact.setCreatedByUserName(username);
        businessPartnerLoanContact = businessPartnerLoanContactRepository.save(businessPartnerLoanContact);

        if (update == true) {
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
        } else{
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
        }
        return businessPartnerLoanContact;
    }
    
}
