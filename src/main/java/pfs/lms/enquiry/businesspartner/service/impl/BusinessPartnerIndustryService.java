package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIndustry;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.IndustryType;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIndustryRepository;
import pfs.lms.enquiry.businesspartner.repository.IndustrySystemRepository;
import pfs.lms.enquiry.businesspartner.repository.IndustryTypeRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIndustryResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIndustryService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerIndustryService implements IBusinessPartnerIndustryService {
    private final IndustryTypeRepository industryTypeRepository;
    private final IndustrySystemRepository industrySystemRepository;

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
        businessPartnerIndustry.setCreatedAt(LocalTime.now());
        businessPartnerIndustry.setCreatedOn(LocalDate.now());
        businessPartnerIndustry.setCreatedByUserName(username);
        partner.setWorkFlowStatusCode(11); //Updated
        partnerRepository.save(partner);

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
        businessPartnerIndustry.setChangedAt(LocalTime.now());
        businessPartnerIndustry.setChangedOn(LocalDate.now());
        businessPartnerIndustry.setChangedByUserName(username);
        Partner partner = businessPartnerIndustry.getPartner();
        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);
        partnerRepository.save(partner);

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


    @Override
    public BusinessPartnerIndustry migrate(BusinessPartnerIndustryMigrationResource businessPartnerIndustryResource, String username) throws CloneNotSupportedException {



        Boolean update = false;
        Object oldObject = new Object();
        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerIndustryResource.getPartnerId()));

        BusinessPartnerIndustry businessPartnerIndustry = new BusinessPartnerIndustry();

        IndustrySystem industrySystem = industrySystemRepository.findIndustrySystemByCode(businessPartnerIndustryResource.getIndustrySystemId().toString());
        IndustryType industryType = industryTypeRepository.findIndustryTypeByCode(businessPartnerIndustryResource.getIndustrySystemId().toString());

        if (partner != null) {
            List<BusinessPartnerIndustry> businessPartnerIndustries =
                    businessPartnerIndustryRepository.findByPartnerIdAndIndustrySystemId(partner.getId(),industrySystem.getId());
            if (businessPartnerIndustries.size() > 0 ){
                businessPartnerIndustry = businessPartnerIndustries.get(0);
                oldObject = businessPartnerIndustry.clone();
                update = true;
            } else {
                businessPartnerIndustry = new BusinessPartnerIndustry();
            }
        } else{
            log.error("Business Partner Master Data Not Found for ID: " + businessPartnerIndustryResource.getPartnerId());
            return null;
        }

        if (update == false){
            List<BusinessPartnerIndustry> businessPartnerIndustryList = businessPartnerIndustryRepository.findByPartnerIdOrderBySerialNumberDesc(partner.getId());
            businessPartnerIndustry.setSerialNumber(businessPartnerIndustryList.size() + 1);
        }

        businessPartnerIndustry.setCreatedAt(LocalTime.now());
        businessPartnerIndustry.setCreatedOn(LocalDate.now());
        businessPartnerIndustry.setCreatedByUserName(username);

        businessPartnerIndustry.setIndustrySystemId(industrySystem.getId());
        businessPartnerIndustry.setIndustryTypeId(industryType.getId());
        businessPartnerIndustry.setPartner(partner);
        businessPartnerIndustry= businessPartnerIndustryRepository.save(businessPartnerIndustry);

        if (update == false) {
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
        } else {
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
        }
        return  businessPartnerIndustry;
    }
}
