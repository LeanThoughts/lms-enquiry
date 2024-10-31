package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIdentificationRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BusinessPartnerIdentificationService implements IBusinessPartnerIdentificationService {

    private final BusinessPartnerIdentificationRepository businessPartnerIdentificationRepository;
    private final PartnerRepository partnerRepository;

    public BusinessPartnerIdentification create(BusinessPartnerIdentificationResource businessPartnerIdentificationResource) {
        Partner partner = partnerRepository.findById(businessPartnerIdentificationResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getPartnerId().toString() 
                + " : Business partner not found"));
        BusinessPartnerIdentification businessPartnerIdentification = new BusinessPartnerIdentification();
        businessPartnerIdentification.setPartner(partner);
        Integer lastSerialNumber = businessPartnerIdentificationRepository.findFirstByPartnerOrderBySerialNumberDesc(partner)
                .map(BusinessPartnerIdentification::getSerialNumber)
                .orElse(0);
        businessPartnerIdentification.setSerialNumber(lastSerialNumber + 1);
        businessPartnerIdentification.setIdentificationCategoryId(businessPartnerIdentificationResource.
                getIdentificationCategoryId());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());
        return businessPartnerIdentificationRepository.save(businessPartnerIdentification);
    }

    @Override
    public BusinessPartnerIdentification update(BusinessPartnerIdentificationResource businessPartnerIdentificationResource) {
        BusinessPartnerIdentification businessPartnerIdentification = businessPartnerIdentificationRepository.findById(businessPartnerIdentificationResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getId().toString() 
                + " : Business partner Industry not found"));
        businessPartnerIdentification.setIdentificationCategoryId(businessPartnerIdentificationResource.
                getIdentificationCategoryId());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());
        return businessPartnerIdentificationRepository.save(businessPartnerIdentification);
    }
}
