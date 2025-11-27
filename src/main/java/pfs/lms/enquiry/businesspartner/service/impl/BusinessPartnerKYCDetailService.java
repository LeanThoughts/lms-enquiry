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

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessPartnerKYCDetailService implements IBusinessPartnerKYCDetailService {

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
        businessPartnerKYCDetail = businessPartnerKYCDetailRepository.save(businessPartnerKYCDetail);

        // Set Partner Workflow Status Code to Updated
//        partner.setWorkFlowStatusCode(11);

//        changeDocumentService.createChangeDocument(
//                businessPartnerFinancial.getId(),
//                businessPartnerFinancial.getId().toString(),
//                businessPartnerFinancial.getPartner().getId().toString(),
//                businessPartnerFinancial.getPartner().getId().toString(),
//                null,
//                businessPartnerFinancial,
//                "Created",
//                username,
//                "Partner", "BusinessPartnerIdentification");

       // updateLoanPartnerKYC(businessPartnerFinancial);


        return businessPartnerKYCDetail;
    }

    @Override
    public BusinessPartnerKYCDetail update(BusinessPartnerKYCDetailResource businessPartnerKYCDetailResource, String username) throws CloneNotSupportedException {
        BusinessPartnerKYCDetail businessPartnerKYCDetail = businessPartnerKYCDetailRepository.findById(businessPartnerKYCDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(businessPartnerKYCDetailResource.getId().toString()
                + " : Business partner kyc detail not found"));

//        Object oldObject = businessPartnerFinancial.clone();

        businessPartnerKYCDetail.setKycDate(businessPartnerKYCDetailResource.getKycDate());
        businessPartnerKYCDetail.setKycRiskCategory(businessPartnerKYCDetailResource.getKycRiskCategory());
        businessPartnerKYCDetail.setReKYCDate(businessPartnerKYCDetailResource.getReKYCDate());
        businessPartnerKYCDetail.setReKYCRiskCategory(businessPartnerKYCDetailResource.getReKYCRiskCategory());
        businessPartnerKYCDetail = businessPartnerKYCDetailRepository.save(businessPartnerKYCDetail);

//        Partner partner = businessPartnerFinancial.getPartner();
//        // Set Partner Workflow Status Code to Updated
//        partner.setWorkFlowStatusCode(11);
//        partnerRepository.save(partner);
//
//        changeDocumentService.createChangeDocument(
//                businessPartnerFinancial.getId(),
//                businessPartnerFinancial.getId().toString(),
//                businessPartnerFinancial.getPartner().getId().toString(),
//                businessPartnerFinancial.getPartner().getId().toString(),
//                oldObject,
//                businessPartnerFinancial,
//                "Updated",
//                username,
//                "Partner", "BusinessPartnerIdentification");
        //updateLoanPartnerKYC(businessPartnerFinancial);

        return businessPartnerKYCDetail;
    }
}
