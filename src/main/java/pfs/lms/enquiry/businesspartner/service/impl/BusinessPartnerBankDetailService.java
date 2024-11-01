package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerLoanContact;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerBankDetailRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerBankDetailService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Service
@RequiredArgsConstructor
public class BusinessPartnerBankDetailService implements IBusinessPartnerBankDetailService {

    private final BusinessPartnerBankDetailRepository businessPartnerBankDetailRepository;
    private final PartnerRepository partnerRepository;
    private final IChangeDocumentService changeDocumentService;

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
        businessPartnerBankDetail.setBankName(businessPartnerBankDetailResource.getBankCountry());
        businessPartnerBankDetail.setIfscCode(businessPartnerBankDetailResource.getIfscCode());
        businessPartnerBankDetail.setAccountNumber(businessPartnerBankDetailResource.getAccountNumber());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setEntryDate(businessPartnerBankDetailResource.getEntryDate());
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
        businessPartnerBankDetail.setBankName(businessPartnerBankDetailResource.getBankCountry());
        businessPartnerBankDetail.setIfscCode(businessPartnerBankDetailResource.getIfscCode());
        businessPartnerBankDetail.setAccountNumber(businessPartnerBankDetailResource.getAccountNumber());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setEntryDate(businessPartnerBankDetailResource.getEntryDate());
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
    
}
