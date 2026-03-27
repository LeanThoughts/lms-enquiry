package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomer;
import pfs.lms.enquiry.appraisal.knowyourcustomer.KnowYourCustomerRepository;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartner;
import pfs.lms.enquiry.appraisal.loanpartner.LoanPartnerRepository;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIdentificationRepository;
import pfs.lms.enquiry.businesspartner.repository.CountryCodeRepository;
import pfs.lms.enquiry.businesspartner.repository.DocumentTypeRepository;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerIdentificationResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.CountryRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.RegionRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerIdentificationService implements IBusinessPartnerIdentificationService {

    private final BusinessPartnerIdentificationRepository businessPartnerIdentificationRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;
    private final IdentificationCategoryRepository identificationCategoryRepository;
    private final LoanPartnerRepository loanPartnerRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final KnowYourCustomerRepository knowYourCustomerRepository;
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final DocumentTypeRepository documentTypeRepository;

    public BusinessPartnerIdentification create(BusinessPartnerIdentificationResource businessPartnerIdentificationResource, String username) {
        Partner partner = partnerRepository.findById(businessPartnerIdentificationResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getPartnerId().toString() 
                + " : Business partner not found"));

        IdentificationCategory identificationCategory = identificationCategoryRepository.findByCode(businessPartnerIdentificationResource.getIdentificationCategoryCode())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getIdentificationCategoryCode()
                + " : Identification category not found"));

        if (identificationCategory.isDuplicateCheckRequired()) {
            List<BusinessPartnerIdentification> existingIdentifications = businessPartnerIdentificationRepository
                    .findByIdentificationCategoryCodeAndIdentificationNumber(identificationCategory.getCode(), businessPartnerIdentificationResource.getIdentificationNumber());
            if (existingIdentifications.size() > 0) {
                if (existingIdentifications.size() == 1) {
                    BusinessPartnerIdentification businessPartnerIdentification1 = existingIdentifications.get(0);
                    if (businessPartnerIdentification1.getPartner().getId() == partner.getId()) {
                    } else {
                        throw new RuntimeException(identificationCategory.getValue() +
                                " is already assigned to another business partner (" + businessPartnerIdentification1.getPartner().getPartyName1() + businessPartnerIdentification1.getPartner().getPartyName() + ")");

                    }
                } else {
                    String partyName1 = existingIdentifications.get(0).getPartner().getPartyName1();
                    throw new RuntimeException(identificationCategory.getValue() + " is already assigned to another business partner (" + partyName1 + ")");
                }
            }
        }

        BusinessPartnerIdentification businessPartnerIdentification = new BusinessPartnerIdentification();
        businessPartnerIdentification.setPartner(partner);
        Integer lastSerialNumber = businessPartnerIdentificationRepository.findFirstByPartnerOrderBySerialNumberDesc(partner)
                .map(BusinessPartnerIdentification::getSerialNumber)
                .orElse(0);
        businessPartnerIdentification.setSerialNumber(lastSerialNumber + 1);

        businessPartnerIdentification.setIdentificationCategoryCode(identificationCategory.getCode());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());
        businessPartnerIdentification.setDocumentType(businessPartnerIdentificationResource.getDocumentType());
        businessPartnerIdentification.setCountry(businessPartnerIdentificationResource.getCountry());
        businessPartnerIdentification.setRegion(businessPartnerIdentificationResource.getRegion());

        businessPartnerIdentification.setCreatedAt(LocalTime.now());
        businessPartnerIdentification.setCreatedOn(LocalDate.now());
        businessPartnerIdentification.setCreatedByUserName(username);
        businessPartnerIdentification = businessPartnerIdentificationRepository.save(businessPartnerIdentification);

        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);

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

       // updateLoanPartnerKYC(businessPartnerIdentification);


        return businessPartnerIdentification;
    }

    @Override
    public BusinessPartnerIdentification update(BusinessPartnerIdentificationResource businessPartnerIdentificationResource, String username) throws CloneNotSupportedException {
        BusinessPartnerIdentification businessPartnerIdentification = businessPartnerIdentificationRepository.findById(businessPartnerIdentificationResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getId().toString() 
                + " : Business partner Industry not found"));

        Object oldObject = businessPartnerIdentification.clone();

        IdentificationCategory identificationCategory = identificationCategoryRepository.findByCode(businessPartnerIdentificationResource.getIdentificationCategoryCode())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerIdentificationResource.getIdentificationCategoryCode()
                        + " : Identification category not found"));

        if (identificationCategory.isDuplicateCheckRequired()) {
            List<BusinessPartnerIdentification> existingIdentifications = businessPartnerIdentificationRepository
                    .findByIdentificationCategoryCodeAndIdentificationNumber(identificationCategory.getCode(), businessPartnerIdentificationResource.getIdentificationNumber());
            if (existingIdentifications.size() > 0) {
                if (existingIdentifications.size() == 1) {
                    BusinessPartnerIdentification businessPartnerIdentification1 = existingIdentifications.get(0);
                    if (businessPartnerIdentification1.getPartner().getId() == businessPartnerIdentification.getPartner().getId()) {
                    } else {
                        throw new RuntimeException(identificationCategory.getValue() +
                                " is already assigned to another business partner (" + businessPartnerIdentification1.getPartner().getPartyName1() + businessPartnerIdentification1.getPartner().getPartyName() + ")");

                    }
                } else {
                    String partyName1 = existingIdentifications.get(0).getPartner().getPartyName1();
                    throw new RuntimeException(identificationCategory.getValue() + " is already assigned to another business partner (" + partyName1 + ")");
                }
            }
        }

        businessPartnerIdentification.setIdentificationCategoryCode(identificationCategory.getCode());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());
        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());
        businessPartnerIdentification.setDocumentType(businessPartnerIdentificationResource.getDocumentType());
        businessPartnerIdentification.setCountry(businessPartnerIdentificationResource.getCountry());
        businessPartnerIdentification.setRegion(businessPartnerIdentificationResource.getRegion());
        businessPartnerIdentification.setChangedAt(LocalTime.now());
        businessPartnerIdentification.setChangedOn(LocalDate.now());
        businessPartnerIdentification.setChangedByUserName(username);
        businessPartnerIdentification = businessPartnerIdentificationRepository.save(businessPartnerIdentification);

        Partner partner = businessPartnerIdentification.getPartner();
        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);
        partnerRepository.save(partner);

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
        //updateLoanPartnerKYC(businessPartnerIdentification);

        return businessPartnerIdentification;
    }


    @Override
    public BusinessPartnerIdentification migrate(BusinessPartnerIdentificationMigrationResource businessPartnerIdentificationResource, String username) throws CloneNotSupportedException {
        BusinessPartnerIdentification businessPartnerIdentification = new BusinessPartnerIdentification();

        IdentificationCategory identificationCategory = identificationCategoryRepository.findByCode(businessPartnerIdentificationResource.getIdentificationCategory()).get();

//        Object idCat = identificationCategoryRepository.findByCode(businessPartnerIdentificationResource.getIdentificationCategory());
//        IdentificationCategory identificationCategory = (IdentificationCategory) idCat;

        if (identificationCategory == null){
            log.error("Identification Category Not Found: " + businessPartnerIdentificationResource.getIdentificationCategory());
            return null;
        }



        Boolean update = false;
        Object oldObject = new Object();
        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerIdentificationResource.getPartnerId()));
        if (partner != null) {
            List<BusinessPartnerIdentification> businessPartnerIdentifications =
                    businessPartnerIdentificationRepository.findByPartnerIdAndIdentificationCategoryCodeAndIdentificationNumber(
                            partner.getId() , identificationCategory.getCode(), businessPartnerIdentificationResource.getIdentificationNumber());
            if (businessPartnerIdentifications.size() > 0 ){
                businessPartnerIdentification = businessPartnerIdentifications.get(0);
                oldObject = businessPartnerIdentification.clone();
                businessPartnerIdentification.setChangedAt(LocalTime.now());
                businessPartnerIdentification.setChangedOn(LocalDate.now());
                businessPartnerIdentification.setChangedByUserName(username);
                update = true;
            } else {
                businessPartnerIdentification = new BusinessPartnerIdentification();
                businessPartnerIdentification.setPartner(partner);
                businessPartnerIdentification.setCreatedAt(LocalTime.now());
                businessPartnerIdentification.setCreatedOn(LocalDate.now());
                businessPartnerIdentification.setCreatedByUserName(username);
            }
        } else{
            log.error("Business Partner Master Data Not Found for ID: " + businessPartnerIdentificationResource.getPartnerId());
            return null;
        }

        List<BusinessPartnerIdentification> businessPartnerIdentificationList = businessPartnerIdentificationRepository.findByPartnerIdOrderBySerialNumberDesc(partner.getId());
        if ( businessPartnerIdentification.getId() == null){
            businessPartnerIdentification.setSerialNumber(businessPartnerIdentificationList.size()+1);
        }

        businessPartnerIdentification.setIdentificationCategoryCode(identificationCategory.getCode());
        businessPartnerIdentification.setIdentificationNumber(businessPartnerIdentificationResource.
                getIdentificationNumber());
        if (businessPartnerIdentificationResource.getIdInstitute() != null && businessPartnerIdentificationResource.getIdInstitute().length() > 0)
        businessPartnerIdentification.setIdInstitute(businessPartnerIdentificationResource.getIdInstitute());
        if (businessPartnerIdentificationResource.getIdEntryDate() != null)
        businessPartnerIdentification.setIdEntryDate(businessPartnerIdentificationResource.getIdEntryDate());
        if (businessPartnerIdentificationResource.getIdValidFromDate() != null)
        businessPartnerIdentification.setIdValidFromDate(businessPartnerIdentificationResource.getIdValidFromDate());
        if (businessPartnerIdentificationResource.getIdValidToDate() != null)
        businessPartnerIdentification.setIdValidToDate(businessPartnerIdentificationResource.getIdValidToDate());

        if ( businessPartnerIdentificationResource.getDocumentName() != null && businessPartnerIdentificationResource.getDocumentName().length() > 0 )
        businessPartnerIdentification.setDocumentName(businessPartnerIdentificationResource.getDocumentName());

        businessPartnerIdentification.setFileReference(businessPartnerIdentificationResource.getFileReference());

        if (businessPartnerIdentificationResource.getDocumentType() !=null  && businessPartnerIdentificationResource.getDocumentType().length() > 0 )
            businessPartnerIdentification.setDocumentType(businessPartnerIdentificationResource.getDocumentType());

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
        log.info("Finished Migrating BusinessPartnerIdentification Partner Number: " + partner.getPartyNumber() + "ID Number:" + businessPartnerIdentification.getIdentificationNumber() );

        //updateLoanPartnerKYC(businessPartnerIdentification);

        return businessPartnerIdentification;

    }

    public BusinessPartnerIdentification updateLoanPartnerKYC( BusinessPartnerIdentification businessPartnerIdentification  ){
        Boolean addKycDocument = false;

//        ZPFSBP0002 PAN Card
//        ZPFSBP0008 PAN Card of Company
//        ZPFSBP0003 Passport
//        ZPFSBP0019 Board Resolution
//        ZPFSBP0005 MoA and Articles of Association (AoA)
//        ZPFSBP0006 Certification of Incorporation
//        ZPFSBP0004 Address Proof
        try {
        if (
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0002")) ||
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0008")) ||
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0003")) ||
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0019")) ||
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0005")) ||
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0006")) ||
            (businessPartnerIdentification.getDocumentType().equals("ZPFSBP0004"))
                    ){
            addKycDocument = true;
        } else {
            return null;
        }
        } catch (Exception ex ){
            log.error("Document Type is empty in Business Partner Identification Number :" + businessPartnerIdentification.getIdentificationNumber()
                       + " Partner Name " + businessPartnerIdentification.getPartner().getPartyName1() +  businessPartnerIdentification.getPartner().getPartyName2() );
        }

        List<LoanApplication> loanApplications =   loanApplicationRepository.findByLoanApplicant(businessPartnerIdentification.getPartner().getId());

        for (LoanApplication loanApplication: loanApplications) {
            List<LoanPartner> loanPartnerList = loanPartnerRepository.findByLoanApplication(loanApplication);
            for(LoanPartner loanPartner: loanPartnerList){
                if (loanPartner.getRoleType().equals("TR0100")){
                    if (loanPartner.getBusinessPartnerId() != null) {
                        List<KnowYourCustomer> knowYourCustomerList = knowYourCustomerRepository.findByLoanPartnerId(loanPartner.getBusinessPartnerId());
                        KnowYourCustomer knowYourCustomer = new KnowYourCustomer();
                        knowYourCustomer = knowYourCustomerRepository.findByLoanPartnerIdAndDocumentType( loanPartner.getId().toString(), businessPartnerIdentification.getDocumentType());
                        if (knowYourCustomer != null) {
                            knowYourCustomer.setDateOfCompletion(businessPartnerIdentification.getIdEntryDate());
                            knowYourCustomer.setDocumentName(businessPartnerIdentification.getDocumentName());
                            knowYourCustomer.setFileReference(businessPartnerIdentification.getFileReference());
                            knowYourCustomer = knowYourCustomerRepository.save(knowYourCustomer);
                        }else {
                            knowYourCustomer = new KnowYourCustomer();
                            knowYourCustomer.setLoanPartnerId(loanPartner.getId().toString());
                            knowYourCustomer.setDateOfCompletion(businessPartnerIdentification.getIdEntryDate());
                            knowYourCustomer.setDocumentName(businessPartnerIdentification.getDocumentName());
                            knowYourCustomer.setFileReference(businessPartnerIdentification.getFileReference());
                            knowYourCustomer.setDocumentType(businessPartnerIdentification.getDocumentType());
                            knowYourCustomer = knowYourCustomerRepository.save(knowYourCustomer);
                        }
                    }
                }
            }

        }

        return businessPartnerIdentification;

    }

    @Override
    public List<BusinessPartnerIdentificationResource> findByPartnerId(UUID partnerId) {
        List<BusinessPartnerIdentification> businessPartnerIdentifications = businessPartnerIdentificationRepository.findByPartnerIdOrderBySerialNumberDesc(partnerId);
        List<BusinessPartnerIdentificationResource> businessPartnerIdentificationResources = new ArrayList<>();
        for (BusinessPartnerIdentification businessPartnerIdentification : businessPartnerIdentifications) {
            BusinessPartnerIdentificationResource businessPartnerIdentificationResource = new BusinessPartnerIdentificationResource();
            businessPartnerIdentificationResource.setId(businessPartnerIdentification.getId());
            businessPartnerIdentificationResource.setSerialNumber(businessPartnerIdentification.getSerialNumber());
            businessPartnerIdentificationResource.setPartnerId(businessPartnerIdentification.getPartner().getId());
            businessPartnerIdentificationResource.setIdentificationCategoryCode(businessPartnerIdentification.getIdentificationCategoryCode());
            businessPartnerIdentificationResource.setIdentificationNumber(businessPartnerIdentification.getIdentificationNumber());
            businessPartnerIdentificationResource.setIdInstitute(businessPartnerIdentification.getIdInstitute());
            businessPartnerIdentificationResource.setIdEntryDate(businessPartnerIdentification.getIdEntryDate());
            businessPartnerIdentificationResource.setIdValidFromDate(businessPartnerIdentification.getIdValidFromDate());
            businessPartnerIdentificationResource.setIdValidToDate(businessPartnerIdentification.getIdValidToDate());
            businessPartnerIdentificationResource.setDocumentName(businessPartnerIdentification.getDocumentName());
            businessPartnerIdentificationResource.setFileReference(businessPartnerIdentification.getFileReference());
            businessPartnerIdentificationResource.setDocumentType(businessPartnerIdentification.getDocumentType());
            businessPartnerIdentificationResource.setCountry(businessPartnerIdentification.getCountry());
            businessPartnerIdentificationResource.setRegion(businessPartnerIdentification.getRegion());

            businessPartnerIdentificationResource.setIdentificationCategory(identificationCategoryRepository.
                    findByCode(businessPartnerIdentification.getIdentificationCategoryCode()).get().getValue());

            if (businessPartnerIdentification.getCountry() != null)
                businessPartnerIdentificationResource.setCountryName(countryRepository.
                        findByCountryCode(businessPartnerIdentification.getCountry()).getValue());

            if (businessPartnerIdentification.getRegion() != null)
                businessPartnerIdentificationResource.setRegionName(regionRepository.findByCountryCodeAnAndRegionCode(businessPartnerIdentification.getCountry(),
                                businessPartnerIdentification.getRegion()).getValue());

            if (businessPartnerIdentification.getDocumentType() != null && businessPartnerIdentification.getDocumentType().length() > 0 )
                businessPartnerIdentificationResource.setDocumentTypeName(documentTypeRepository.
                        findByCode(businessPartnerIdentification.getDocumentType()).getDescription());

            businessPartnerIdentificationResources.add(businessPartnerIdentificationResource);
        }
        return businessPartnerIdentificationResources;
    }
}
