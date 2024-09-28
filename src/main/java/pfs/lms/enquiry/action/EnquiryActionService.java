package pfs.lms.enquiry.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnquiryActionService implements  IEnquiryActionService{

    private final IChangeDocumentService changeDocumentService;
    private final EnquiryActionRepository enquiryActionRepository;

    @Override
    public EnquiryAction processRejection(EnquiryAction enquiryAction, String username) throws CloneNotSupportedException {
        Object oldEnquiryAction = enquiryAction.clone();
        enquiryAction.setWorkFlowStatusCode(04);
        enquiryAction.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                enquiryAction.getId(), enquiryAction.getId().toString(), null,
                enquiryAction.getLoanApplication().getLoanContractId(),
                oldEnquiryAction,
                enquiryAction,
                "Updated",
                username,
                "Process Enquiry", "Header");
        enquiryActionRepository.save(enquiryAction);

        return enquiryAction;
    }
    }

