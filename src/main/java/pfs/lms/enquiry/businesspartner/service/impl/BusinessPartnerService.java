package pfs.lms.enquiry.businesspartner.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.businesspartner.service.IBusinessPartnerService;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Service
@RequiredArgsConstructor
public class BusinessPartnerService implements IBusinessPartnerService {

    private final IChangeDocumentService changeDocumentService;
    private final PartnerRepository partnerRepository;
    @Override
    public Partner updatePartnerAfterApproval(Partner partner, String username) throws CloneNotSupportedException {

        //TODO Trigger SAP Integration

        return null;
    }

    @Override
    public Partner updatePartnerAfterRejection(Partner partner, String username) throws CloneNotSupportedException {

        Object oldPartner = partner.clone();
        partner.setWorkFlowStatusCode(04);
        partner.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
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

