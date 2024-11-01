package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIndustryRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIndustryService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Service
@RequiredArgsConstructor
public class BusinessPartnerIndustryService implements IBusinessPartnerIndustryService {

    private final BusinessPartnerIndustryRepository businessPartnerIndustryRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;

    public BusinessPartnerIndustry create(BusinessPartnerIndustryResource businessPartnerIndustryResource,String username) {
        Partner partner = partnerRepository.findById(businessPartnerIndustryResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIndustryResource.getPartnerId().toString() 
                + " : Business partner not found"));
        BusinessPartnerIndustry businessPartnerIndustry = new BusinessPartnerIndustry();
        businessPartnerIndustry.setPartner(partner);
        Integer lastSerialNumber = businessPartnerIndustryRepository.findFirstByPartnerOrderBySerialNumberDesc(partner)
                .map(BusinessPartnerIndustry::getSerialNumber)
                .orElse(0);
        businessPartnerIndustry.setSerialNumber(lastSerialNumber + 1);
        businessPartnerIndustry.setIndustrySystemId(businessPartnerIndustryResource.getIndustrySystemId());
        businessPartnerIndustry.setIndustryTypeId(businessPartnerIndustryResource.getIndustryTypeId());
        businessPartnerIndustry = businessPartnerIndustryRepository.save(businessPartnerIndustry);

        changeDocumentService.createChangeDocument(
                businessPartnerIndustry.getId(),
                businessPartnerIndustry.getId().toString(),
                businessPartnerIndustry.getPartner().getId().toString(),
                businessPartnerIndustry.getPartner().getId().toString(),
                null,
                businessPartnerIndustry,
                "Created",
                username,
                "Partner", "BusinessPartnerIndustry");

        return businessPartnerIndustry;
    }

    @Override
    public BusinessPartnerIndustry update(BusinessPartnerIndustryResource businessPartnerIndustryResource, String username) throws CloneNotSupportedException {
        BusinessPartnerIndustry businessPartnerIndustry = businessPartnerIndustryRepository.findById(businessPartnerIndustryResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIndustryResource.getId().toString() 
                + " : Business partner Industry not found"));

        Object oldObject = businessPartnerIndustry.clone();

        businessPartnerIndustry.setIndustrySystemId(businessPartnerIndustryResource.getIndustrySystemId());
        businessPartnerIndustry.setIndustryTypeId(businessPartnerIndustryResource.getIndustryTypeId());
        businessPartnerIndustry= businessPartnerIndustryRepository.save(businessPartnerIndustry);


        changeDocumentService.createChangeDocument(
                businessPartnerIndustry.getId(),
                businessPartnerIndustry.getId().toString(),
                businessPartnerIndustry.getPartner().getId().toString(),
                businessPartnerIndustry.getPartner().getId().toString(),
                oldObject,
                businessPartnerIndustry,
                "Updated",
                username,
                "Partner", "BusinessPartnerIndustry");

        return  businessPartnerIndustry;
    }
}
