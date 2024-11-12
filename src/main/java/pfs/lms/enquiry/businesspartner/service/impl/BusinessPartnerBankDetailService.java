package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerBankDetailRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailMigrationResource;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerBankDetailService;
import pfs.lms.enquiry.domain.BankMaster;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.BankMasterRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessPartnerBankDetailService implements IBusinessPartnerBankDetailService {

    private final BusinessPartnerBankDetailRepository businessPartnerBankDetailRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;
    private final BankMasterRepository bankMasterRepository;

    public BusinessPartnerBankDetail create(BusinessPartnerBankDetailResource businessPartnerBankDetailResource , String username) {
        Partner partner = partnerRepository.findById(businessPartnerBankDetailResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerBankDetailResource.getPartnerId().toString() 
                + " : Partner not found"));
        BusinessPartnerBankDetail businessPartnerBankDetail = new BusinessPartnerBankDetail();
        businessPartnerBankDetail.setPartner(partner);
        Integer lastSerialNumber = businessPartnerBankDetailRepository.findFirstByPartnerOrderBySerialNumberDesc(partner)
                .map(BusinessPartnerBankDetail::getSerialNumber)
                .orElse(0);
        businessPartnerBankDetail.setSerialNumber(lastSerialNumber + 1);
        businessPartnerBankDetail.setBankKey(businessPartnerBankDetailResource.getBankKey());
        businessPartnerBankDetail.setBankName(businessPartnerBankDetailResource.getBankName());
        businessPartnerBankDetail.setIfscCode(businessPartnerBankDetailResource.getIfscCode());
        businessPartnerBankDetail.setAccountNumber(businessPartnerBankDetailResource.getAccountNumber());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setEntryDate(businessPartnerBankDetailResource.getEntryDate());
        businessPartnerBankDetail.setBankCountry(businessPartnerBankDetailResource.getBankCountry());
        businessPartnerBankDetail.setReferenceNumber(businessPartnerBankDetailResource.getReferenceNumber());
        businessPartnerBankDetail.setAccountHolderName(businessPartnerBankDetailResource.getAccountHolderName());
        businessPartnerBankDetail.setBankAccountName(businessPartnerBankDetailResource.getBankAccountName());
        businessPartnerBankDetail = businessPartnerBankDetailRepository.save(businessPartnerBankDetail);


        changeDocumentService.createChangeDocument(
                businessPartnerBankDetail.getId(),
                businessPartnerBankDetail.getId().toString(),
                partner.getId().toString(),
                partner.getId().toString(),
                null,
                businessPartnerBankDetail,
                "Created",
                username,
                "Partner", "BusinessPartnerBankDetail");

        return businessPartnerBankDetail;
    }

    @Override
    public BusinessPartnerBankDetail update(BusinessPartnerBankDetailResource businessPartnerBankDetailResource, String username) throws CloneNotSupportedException {

        BusinessPartnerBankDetail businessPartnerBankDetail =
                businessPartnerBankDetailRepository.findById(businessPartnerBankDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerBankDetailResource.getId().toString() 
                + " : Business Partner Bank Detail not found"));

        Object oldObject = businessPartnerBankDetail.clone();

        businessPartnerBankDetail.setBankKey(businessPartnerBankDetailResource.getBankKey());
        businessPartnerBankDetail.setBankName(businessPartnerBankDetailResource.getBankName());
        businessPartnerBankDetail.setIfscCode(businessPartnerBankDetailResource.getIfscCode());
        businessPartnerBankDetail.setAccountNumber(businessPartnerBankDetailResource.getAccountNumber());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setEntryDate(businessPartnerBankDetailResource.getEntryDate());
        businessPartnerBankDetail.setBankCountry(businessPartnerBankDetailResource.getBankCountry());
        businessPartnerBankDetail.setReferenceNumber(businessPartnerBankDetailResource.getReferenceNumber());
        businessPartnerBankDetail.setAccountHolderName(businessPartnerBankDetailResource.getAccountHolderName());
        businessPartnerBankDetail.setBankAccountName(businessPartnerBankDetailResource.getBankAccountName());

        businessPartnerBankDetail = businessPartnerBankDetailRepository.save(businessPartnerBankDetail);

        changeDocumentService.createChangeDocument(
                businessPartnerBankDetail.getId(),
                businessPartnerBankDetail.getId().toString(),
                businessPartnerBankDetail.getPartner().getId().toString(),
                businessPartnerBankDetail.getPartner().getId().toString(),
                oldObject,
                businessPartnerBankDetail,
                "Updated",
                username,
                "Partner", "BusinessPartnerBankDetail");

        return businessPartnerBankDetail;
    }

    @Override
    public BusinessPartnerBankDetail migrate(BusinessPartnerBankDetailMigrationResource businessPartnerBankDetailResource, String username) throws CloneNotSupportedException {
        BusinessPartnerBankDetail businessPartnerBankDetail = new BusinessPartnerBankDetail();
        Boolean update = false;
        Object oldObject = new Object();
        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(businessPartnerBankDetailResource.getPartnerId()));
        if (partner != null) {
             businessPartnerBankDetail =
                    businessPartnerBankDetailRepository.findByPartnerIdAndSerialNumber(
                            partner.getId(), businessPartnerBankDetailResource.getSerialNumber());
             if (businessPartnerBankDetail != null){
                    oldObject = businessPartnerBankDetail.clone();
                    update = true;
             } else {
                 businessPartnerBankDetail = new BusinessPartnerBankDetail();
             }
        } else{
            log.error("Business Partner Master Data Not Found for ID: " + businessPartnerBankDetailResource.getPartnerId());
            return null;
        }

        List<BankMaster> bankMasters = bankMasterRepository.findByBankKey(businessPartnerBankDetailResource.getBankKey());
        if (bankMasters.size() > 0) {
            BankMaster bankMaster = bankMasters.get(0);
            businessPartnerBankDetail.setBankName(bankMaster.getBankName());
        }
        businessPartnerBankDetail.setSerialNumber(businessPartnerBankDetailResource.getSerialNumber());
        businessPartnerBankDetail.setBankKey(businessPartnerBankDetailResource.getBankKey());
        businessPartnerBankDetail.setIfscCode(businessPartnerBankDetailResource.getIfscCode());
        businessPartnerBankDetail.setAccountNumber(businessPartnerBankDetailResource.getAccountNumber());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setEntryDate(businessPartnerBankDetailResource.getEntryDate());
        businessPartnerBankDetail.setPartner(partner);
        businessPartnerBankDetail = businessPartnerBankDetailRepository.save(businessPartnerBankDetail);

        if (update == true) {
            log.info("Updating Change Document OLD OBJECT:  " + oldObject.toString());
            log.info("Updating Change Document NEW OBJECT:  " + businessPartnerBankDetail.toString());

            changeDocumentService.createChangeDocument(
                    businessPartnerBankDetail.getId(),
                    businessPartnerBankDetail.getId().toString(),
                    businessPartnerBankDetail.getPartner().getId().toString(),
                    businessPartnerBankDetail.getPartner().getId().toString(),
                    oldObject,
                    businessPartnerBankDetail,
                    "Updated",
                    username,
                    "Partner", "BusinessPartnerBankDetail");
        } else {
            log.info("Creating Change Document :  " + businessPartnerBankDetail.toString());

            changeDocumentService.createChangeDocument(
                    businessPartnerBankDetail.getId(),
                    businessPartnerBankDetail.getId().toString(),
                    businessPartnerBankDetail.getPartner().getId().toString(),
                    businessPartnerBankDetail.getPartner().getId().toString(),
                    null,
                    businessPartnerBankDetail,
                    "Created",
                    username,
                    "Partner", "BusinessPartnerBankDetail");
        }
        log.info("Finished Migrating BusinessPartnerIdentification");
        return businessPartnerBankDetail;
    }
}
