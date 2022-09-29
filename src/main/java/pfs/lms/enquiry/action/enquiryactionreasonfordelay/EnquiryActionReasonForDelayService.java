package pfs.lms.enquiry.action.enquiryactionreasonfordelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnquiryActionReasonForDelayService implements IEnquiryActionReasonForDelayService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final EnquiryActionReasonForDelayRepository enquiryActionReasonForDelayRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public EnquiryActionReasonForDelay create(EnquiryActionReasonForDelayResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    EnquiryAction obj = new EnquiryAction();
                    obj.setLoanApplication(loanApplication);
                    obj = enquiryActionRepository.save(obj);
                    // Change Documents for EnquiryAction Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "EnquiryAction", "Header");
                    return obj;
                });
        EnquiryActionReasonForDelay enquiryActionReasonForDelay = new EnquiryActionReasonForDelay();
        enquiryActionReasonForDelay.setEnquiryAction(enquiryAction);
        enquiryActionReasonForDelay.setDate(resource.getDate());
        enquiryActionReasonForDelay.setReason(resource.getReason());
        enquiryActionReasonForDelay = enquiryActionReasonForDelayRepository.save(enquiryActionReasonForDelay);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                enquiryActionReasonForDelay.getEnquiryAction().getId(),
//                enquiryActionReasonForDelay.getId().toString(),
//                enquiryActionReasonForDelay.getEnquiryAction().getId().toString(),
//                enquiryActionReasonForDelay.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                null,
//                enquiryActionReasonForDelay,
//                "Created",
//                username,
//                "EnquiryAction", "Reason For Delay" );

        return enquiryActionReasonForDelay;
    }

    @Override
    public EnquiryActionReasonForDelay update(EnquiryActionReasonForDelayResource resource, String username)
            throws CloneNotSupportedException {
        EnquiryActionReasonForDelay enquiryActionReasonForDelay =
                enquiryActionReasonForDelayRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = enquiryActionReasonForDelay.clone();

        enquiryActionReasonForDelay.setDate(resource.getDate());
        enquiryActionReasonForDelay.setReason(resource.getReason());
        enquiryActionReasonForDelay = enquiryActionReasonForDelayRepository.save(enquiryActionReasonForDelay);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                enquiryActionReasonForDelay.getEnquiryAction().getId(),
//                enquiryActionReasonForDelay.getId().toString(),
//                enquiryActionReasonForDelay.getEnquiryAction().getId().toString(),
//                enquiryActionReasonForDelay.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                oldReasonForDelay,
//                enquiryActionReasonForDelay,
//                "Updated",
//                username,
//                "EnquiryAction", "Reason For Delay" );

        return enquiryActionReasonForDelay;
    }
}
