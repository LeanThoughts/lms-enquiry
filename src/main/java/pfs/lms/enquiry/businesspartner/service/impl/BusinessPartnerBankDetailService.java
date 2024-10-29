package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerBankDetail;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerBankDetailRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerBankDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerBankDetailService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;

@Service
@RequiredArgsConstructor
public class BusinessPartnerBankDetailService implements IBusinessPartnerBankDetailService {

    private final BusinessPartnerBankDetailRepository businessPartnerBankDetailRepository;
    private final PartnerRepository partnerRepository;

    public BusinessPartnerBankDetail create(BusinessPartnerBankDetailResource businessPartnerBankDetailResource) {
        Partner partner = partnerRepository.findById(businessPartnerBankDetailResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerBankDetailResource.getPartnerId().toString() 
                + " : Partner not found"));
        BusinessPartnerBankDetail businessPartnerBankDetail = new BusinessPartnerBankDetail();
        businessPartnerBankDetail.setPartner(partner);
        businessPartnerBankDetail.setBankKey(businessPartnerBankDetailResource.getBankKey());
        businessPartnerBankDetail.setBankCountry(businessPartnerBankDetailResource.getBankCountry());
        businessPartnerBankDetail.setBankCountryIso(businessPartnerBankDetailResource.getBankCountryIso());
        businessPartnerBankDetail.setBankAccountNumber(businessPartnerBankDetailResource.getBankAccountNumber());
        businessPartnerBankDetail.setControlKey(businessPartnerBankDetailResource.getControlKey());
        businessPartnerBankDetail.setReferenceNumber(businessPartnerBankDetailResource.getReferenceNumber());
        businessPartnerBankDetail.setAccountHolderName(businessPartnerBankDetailResource.getAccountHolderName());
        businessPartnerBankDetail.setCollectionAuthorization(businessPartnerBankDetailResource.getCollectionAuthorization());
        businessPartnerBankDetail.setExternalBankId(businessPartnerBankDetailResource.getExternalBankId());
        businessPartnerBankDetail.setBankAccountName(businessPartnerBankDetailResource.getBankAccountName());
        businessPartnerBankDetail.setIBan(businessPartnerBankDetailResource.getIBan());
        businessPartnerBankDetail.setIBanFromDate(businessPartnerBankDetailResource.getIBanFromDate());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setMoveDate(businessPartnerBankDetailResource.getMoveDate());
        businessPartnerBankDetail.setMoveId(businessPartnerBankDetailResource.getMoveId());
        return businessPartnerBankDetailRepository.save(businessPartnerBankDetail);
    }

    @Override
    public BusinessPartnerBankDetail update(BusinessPartnerBankDetailResource businessPartnerBankDetailResource) {
        BusinessPartnerBankDetail businessPartnerBankDetail = businessPartnerBankDetailRepository.findById(businessPartnerBankDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerBankDetailResource.getId().toString() 
                + " : Business Partner Bank Detail not found"));
        businessPartnerBankDetail.setBankKey(businessPartnerBankDetailResource.getBankKey());
        businessPartnerBankDetail.setBankCountry(businessPartnerBankDetailResource.getBankCountry());
        businessPartnerBankDetail.setBankCountryIso(businessPartnerBankDetailResource.getBankCountryIso());
        businessPartnerBankDetail.setBankAccountNumber(businessPartnerBankDetailResource.getBankAccountNumber());
        businessPartnerBankDetail.setControlKey(businessPartnerBankDetailResource.getControlKey());
        businessPartnerBankDetail.setReferenceNumber(businessPartnerBankDetailResource.getReferenceNumber());
        businessPartnerBankDetail.setAccountHolderName(businessPartnerBankDetailResource.getAccountHolderName());
        businessPartnerBankDetail.setCollectionAuthorization(businessPartnerBankDetailResource.getCollectionAuthorization());
        businessPartnerBankDetail.setExternalBankId(businessPartnerBankDetailResource.getExternalBankId());
        businessPartnerBankDetail.setBankAccountName(businessPartnerBankDetailResource.getBankAccountName());
        businessPartnerBankDetail.setIBan(businessPartnerBankDetailResource.getIBan());
        businessPartnerBankDetail.setIBanFromDate(businessPartnerBankDetailResource.getIBanFromDate());
        businessPartnerBankDetail.setValidFromDate(businessPartnerBankDetailResource.getValidFromDate());
        businessPartnerBankDetail.setValidToDate(businessPartnerBankDetailResource.getValidToDate());
        businessPartnerBankDetail.setMoveDate(businessPartnerBankDetailResource.getMoveDate());
        businessPartnerBankDetail.setMoveId(businessPartnerBankDetailResource.getMoveId());
        return businessPartnerBankDetailRepository.save(businessPartnerBankDetail);
    }
    
}
