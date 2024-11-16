package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIdentificationRepository;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerIdentificationService implements IBusinessPartnerIdentificationService {

    private final BusinessPartnerIdentificationRepository businessPartnerIdentificationRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;
    private final IdentificationCategoryRepository identificationCategoryRepository;

    public BusinessPartnerIdentification create(BusinessPartnerIdentificationResource businessPartnerIdentificationResource, String username) {
        Partner partner = partnerRepository.findById(businessPartnerIdentificationResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getPartnerId().toString() 
                + " : Business partner not found"));

        IdentificationCategory identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode(businessPartnerIdentificationResource.getIdentificationCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getIdentificationCategoryId().toString() 
                + " : Identification category not found"));
        
        if (identificationCategory.isDuplicateCheckRequired()) {
            List<BusinessPartnerIdentification> existingIdentifications = businessPartnerIdentificationRepository
                .findByIdentificationCategoryId(identificationCategory.getId());
            if (existingIdentifications != null) {
                String partyName1 = existingIdentifications.get(0).getPartner().getPartyName1();
                throw new RuntimeException(identificationCategory.getValue() + " is already assigned to another business partner (" + partyName1 + ")");
            }
        }

        BusinessPartnerIdentification businessPartnerIdentification = new BusinessPartnerIdentification();
        businessPartnerIdentification.setPartner(partner);
        Integer lastSerialNumber = businessPartnerIdentificationRepository.findFirstByPartnerOrderBySerialNumberDesc(partner)
                .map(BusinessPartnerIdentification::getSerialNumber)
                .orElse(0);
        businessPartnerIdentification.setSerialNumber(lastSerialNumber + 1);

        businessPartnerIdentification.setIdentificationCategoryId(identificationCategory.getId());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());

        businessPartnerIdentification = businessPartnerIdentificationRepository.save(businessPartnerIdentification);

        changeDocumentService.createChangeDocument(
                businessPartnerIdentification.getId(),
                businessPartnerIdentification.getId().toString(),
                businessPartnerIdentification.getPartner().getId().toString(),
                businessPartnerIdentification.getPartner().getId().toString(),
                null,
                businessPartnerIdentification,
                "Created",
                username,
                "Partner", "BusinessPartnerIdentification");


        return businessPartnerIdentification;
    }

    @Override
    public BusinessPartnerIdentification update(BusinessPartnerIdentificationResource businessPartnerIdentificationResource, String username) throws CloneNotSupportedException {
        BusinessPartnerIdentification businessPartnerIdentification = businessPartnerIdentificationRepository.findById(businessPartnerIdentificationResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getId().toString() 
                + " : Business partner Industry not found"));

        Object oldObject = businessPartnerIdentification.clone();

        IdentificationCategory identificationCategory = identificationCategoryRepository.findIdentificationCategoryByCode(businessPartnerIdentificationResource.getIdentificationCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getIdentificationCategoryId().toString()
                        + " : Identification category not found"));

        if (identificationCategory.isDuplicateCheckRequired()) {
            List<BusinessPartnerIdentification> existingIdentifications = businessPartnerIdentificationRepository
                    .findByIdentificationCategoryId(identificationCategory.getId());
            if (existingIdentifications != null) {
                String partyName1 = existingIdentifications.get(0).getPartner().getPartyName1();
                throw new RuntimeException(identificationCategory.getValue() + " is already assigned to another business partner (" + partyName1 + ")");
            }
        }

        businessPartnerIdentification.setIdentificationCategoryId(identificationCategory.getId());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());

        businessPartnerIdentification = businessPartnerIdentificationRepository.save(businessPartnerIdentification);

        changeDocumentService.createChangeDocument(
                businessPartnerIdentification.getId(),
                businessPartnerIdentification.getId().toString(),
                businessPartnerIdentification.getPartner().getId().toString(),
                businessPartnerIdentification.getPartner().getId().toString(),
                oldObject,
                businessPartnerIdentification,
                "Updated",
                username,
                "Partner", "BusinessPartnerIdentification");

        return businessPartnerIdentification;
    }

    @Override
    public BusinessPartnerIdentification migrate(BusinessPartnerIdentificationMigrationResource businessPartnerIdentificationResource, String username) throws CloneNotSupportedException {
        BusinessPartnerIdentification businessPartnerIdentification = new BusinessPartnerIdentification();

        Object idCat = identificationCategoryRepository.findIdentificationCategoryByCode(businessPartnerIdentificationResource.getIdentificationCategory());
        IdentificationCategory identificationCategory = (IdentificationCategory) idCat;

        if (identificationCategory == null){
            log.error("Identification Category Not Found: " + businessPartnerIdentificationResource.getIdentificationCategory());
            return null;
        }



        Boolean update = false;
        Object oldObject = new Object();
        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerIdentificationResource.getPartnerId()));
        if (partner != null) {
            List<BusinessPartnerIdentification> businessPartnerIdentifications =
                    businessPartnerIdentificationRepository.findByPartnerIdAndIdentificationCategoryIdAndIdentificationNumber(
                            partner.getId() , identificationCategory.getId(), businessPartnerIdentificationResource.getIdentificationNumber());
            if (businessPartnerIdentifications.size() > 0 ){
                businessPartnerIdentification = businessPartnerIdentifications.get(0);
                oldObject = businessPartnerIdentification.clone();
                update = true;
            } else {
                businessPartnerIdentification = new BusinessPartnerIdentification();
            }
        } else{
            log.error("Business Partner Master Data Not Found for ID: " + businessPartnerIdentificationResource.getPartnerId());
            return null;
        }

        List<BusinessPartnerIdentification> businessPartnerIdentificationList = businessPartnerIdentificationRepository.findByPartnerIdOrderBySerialNumberDesc(partner.getId());
        if ( businessPartnerIdentification.getId() == null){
            businessPartnerIdentification.setSerialNumber(businessPartnerIdentificationList.size()+1);
        }

        businessPartnerIdentification.setIdentificationCategoryId(identificationCategory.getId());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());
        businessPartnerIdentification.setPartner(partner);
        businessPartnerIdentification = businessPartnerIdentificationRepository.save(businessPartnerIdentification);

        if (update == true) {
            changeDocumentService.createChangeDocument(
                    businessPartnerIdentification.getId(),
                    businessPartnerIdentification.getId().toString(),
                    businessPartnerIdentification.getPartner().getId().toString(),
                    businessPartnerIdentification.getPartner().getId().toString(),
                    oldObject,
                    businessPartnerIdentification,
                    "Updated",
                    username,
                    "Partner", "BusinessPartnerIdentification");
        } else {
            changeDocumentService.createChangeDocument(
                    businessPartnerIdentification.getId(),
                    businessPartnerIdentification.getId().toString(),
                    businessPartnerIdentification.getPartner().getId().toString(),
                    businessPartnerIdentification.getPartner().getId().toString(),
                    null,
                    businessPartnerIdentification,
                    "Created",
                    username,
                    "Partner", "BusinessPartnerIdentification");
        }
        log.info("Finished Migrating BusinessPartnerIdentification");

        return businessPartnerIdentification;

    }
}
