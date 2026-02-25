package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerKYCDetail;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerKYCDetailRepository;
import pfs.lms.enquiry.businesspartner.resource.BusinessPartnerKYCDetailResource;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerKYCDetailService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerKYCDetailService implements IBusinessPartnerKYCDetailService {
    private final IChangeDocumentService changeDocumentService;

    private final BusinessPartnerKYCDetailRepository businessPartnerKYCDetailRepository;
    private final PartnerRepository partnerRepository;

    public BusinessPartnerKYCDetail create(BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource, String username) {
        Partner partner = partnerRepository.findById(businessPartnerKYCDetailResource.getPartnerId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerKYCDetailResource.getPartnerId().toString()
                + " : Business partner not found"));

        BusinessPartnerKYCDetail businessPartnerKYCDetail = new BusinessPartnerKYCDetail();
        businessPartnerKYCDetail.setPartner(partner);

        businessPartnerKYCDetail.setKycDate(businessPartnerKYCDetailResource.getKycDate());
        businessPartnerKYCDetail.setKycRiskCategory(businessPartnerKYCDetailResource.getKycRiskCategory());
        businessPartnerKYCDetail.setReKYCDate(businessPartnerKYCDetailResource.getReKYCDate());
        businessPartnerKYCDetail.setReKYCRiskCategory(businessPartnerKYCDetailResource.getReKYCRiskCategory());
        businessPartnerKYCDetail.setCreatedAt(LocalTime.now());
        businessPartnerKYCDetail.setCreatedOn(LocalDate.now());
        businessPartnerKYCDetail.setCreatedByUserName(username);
        businessPartnerKYCDetail = businessPartnerKYCDetailRepository.save(businessPartnerKYCDetail);

        // Set Partner Workflow Status Code to Updated
//        partner.setWorkFlowStatusCode(11);

        changeDocumentService.createChangeDocument(
                businessPartnerKYCDetail.getId(),
                businessPartnerKYCDetail.getId().toString(),
                businessPartnerKYCDetail.getPartner().getId().toString(),
                businessPartnerKYCDetail.getPartner().getId().toString(),
                null,
                businessPartnerKYCDetail,
                "Created",
                username,
                "Partner", "BusinessPartnerIdentification");

       // updateLoanPartnerKYC(businessPartnerFinancial);


        return businessPartnerKYCDetail;
    }

    @Override
    public BusinessPartnerKYCDetail update(BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource, String username) throws CloneNotSupportedException {
        BusinessPartnerKYCDetail businessPartnerKYCDetail = businessPartnerKYCDetailRepository.findById(businessPartnerKYCDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerKYCDetailResource.getId().toString()
                + " : Business partner kyc detail not found"));

        Object oldObject = businessPartnerKYCDetail.clone();

        businessPartnerKYCDetail.setKycDate(businessPartnerKYCDetailResource.getKycDate());
        businessPartnerKYCDetail.setKycRiskCategory(businessPartnerKYCDetailResource.getKycRiskCategory());
        businessPartnerKYCDetail.setReKYCDate(businessPartnerKYCDetailResource.getReKYCDate());
        businessPartnerKYCDetail.setReKYCRiskCategory(businessPartnerKYCDetailResource.getReKYCRiskCategory());

        businessPartnerKYCDetail.setChangedAt(LocalTime.now());
        businessPartnerKYCDetail.setChangedOn(LocalDate.now());
        businessPartnerKYCDetail.setChangedByUserName(username);
        businessPartnerKYCDetail = businessPartnerKYCDetailRepository.save(businessPartnerKYCDetail);

        Partner partner = businessPartnerKYCDetail.getPartner();
        // Set Partner Workflow Status Code to Updated
        partner.setWorkFlowStatusCode(11);
        partnerRepository.save(partner);

        changeDocumentService.createChangeDocument(
                businessPartnerKYCDetail.getId(),
                businessPartnerKYCDetail.getId().toString(),
                businessPartnerKYCDetail.getPartner().getId().toString(),
                businessPartnerKYCDetail.getPartner().getId().toString(),
                oldObject,
                businessPartnerKYCDetail,
                "Updated",
                username,
                "Partner", "BusinessPartnerKYCDetail");

        //updateLoanPartnerKYC(businessPartnerKYCDetail);

        return businessPartnerKYCDetail;
    }
}
