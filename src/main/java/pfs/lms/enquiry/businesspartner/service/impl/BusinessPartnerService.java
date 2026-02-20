package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.domain.BusinessPartnerIdentification;
import pfs.lms.enquiry.businesspartner.repository.BusinessPartnerIdentificationRepository;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerIdentificationService;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessPartnerService implements IBusinessPartnerService {

    private final IChangeDocumentService changeDocumentService;
    private final PartnerRepository partnerRepository;
    private final BusinessPartnerIdentificationRepository businessPartnerIdentificationRepository;
    private final IBusinessPartnerIdentificationService businessPartnerIdentificationService;
    @Override
    public Partner updatePartnerAfterApproval(Partner partner, String username) throws CloneNotSupportedException {


        Partner oldPartner = partnerRepository.getOne(partner.getId());
        // Change Documents
        changeDocumentService.createChangeDocument(
                partner.getId(), partner.getId().toString(), null,
                partner.getId().toString(),
                oldPartner,
                partner,
                "Updated",
                username,
                "Partner", "Partner");
        partner.setCreatedAt(LocalTime.now());
        partner.setCreatedOn(LocalDate.now());
        partner.setCreatedByUserName(username);
        partnerRepository.save(partner);

        //Update KYC with the Identification
        List<BusinessPartnerIdentification> businessPartnerIdentificationLIst
                = businessPartnerIdentificationRepository.findByPartnerIdOrderBySerialNumberDesc(partner.getId());
        for (BusinessPartnerIdentification businessPartnerIdentification:businessPartnerIdentificationLIst
        ) {
            businessPartnerIdentificationService.updateLoanPartnerKYC(businessPartnerIdentification);
        }

        return partner;
    }

    @Override
    public Partner updatePartnerAfterRejection(Partner partner, String username) throws CloneNotSupportedException {

        Object oldPartner = partner.clone();
        partner.setWorkFlowStatusCode(04);
        partner.setWorkFlowStatusDescription("Rejected");
        partner.setChangedAt(LocalTime.now());
        partner.setChangedOn(LocalDate.now());
        partner.setChangedByUserName(username);

        // Change Documents
        changeDocumentService.createChangeDocument(
                partner.getId(), partner.getId().toString(), null,
                partner.getId().toString(),
                oldPartner,
                partner,
                "Updated",
                username,
                "Partner", "Partner");
        partnerRepository.save(partner);

        return partner;
    }
}

