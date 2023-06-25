package pfs.lms.enquiry.action.enquirycompletion;

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
public class EnquiryCompletionService implements IEnquiryCompletionService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final EnquiryCompletionRepository enquiryCompletionRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public EnquiryCompletion create(EnquiryCompletionResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    EnquiryAction obj = new EnquiryAction();
                    obj.setLoanApplication(loanApplication);
                    obj = enquiryActionRepository.save(obj);
                    // Change Documents for EnquiryAction Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "EnquiryAction", "Header");
                    return obj;
                });
        EnquiryCompletion enquiryCompletion = new EnquiryCompletion();
        enquiryCompletion.setEnquiryAction(enquiryAction);
        enquiryCompletion.setProductType(resource.getProductType());
        enquiryCompletion.setTerm(resource.getTerm());
        enquiryCompletion.setRemarks(resource.getRemarks());
        enquiryCompletion.setDate(resource.getDate());
        enquiryCompletion = enquiryCompletionRepository.save(enquiryCompletion);

        // Change Documents for Enquiry Completion
        changeDocumentService.createChangeDocument(
                enquiryCompletion.getEnquiryAction().getId(),
                enquiryCompletion.getId().toString(),
                enquiryCompletion.getEnquiryAction().getId().toString(),
                enquiryCompletion.getEnquiryAction().getLoanApplication().getLoanContractId(),
                null,
                enquiryCompletion,
                "Created",
                username,
                "EnquiryAction", "Enquiry Completion" );

        return enquiryCompletion;
    }

    @Override
    public EnquiryCompletion update(EnquiryCompletionResource resource, String username)
            throws CloneNotSupportedException {
        EnquiryCompletion enquiryCompletion =
                enquiryCompletionRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldRejectByPFS = enquiryCompletion.clone();

        enquiryCompletion.setProductType(resource.getProductType());
        enquiryCompletion.setTerm(resource.getTerm());
        enquiryCompletion.setRemarks(resource.getRemarks());
        enquiryCompletion.setDate(resource.getDate());
        enquiryCompletion = enquiryCompletionRepository.save(enquiryCompletion);

        // Change Documents for Enquiry Completion
        changeDocumentService.createChangeDocument(
                enquiryCompletion.getEnquiryAction().getId(),
                enquiryCompletion.getId().toString(),
                enquiryCompletion.getEnquiryAction().getId().toString(),
                enquiryCompletion.getEnquiryAction().getLoanApplication().getLoanContractId(),
                oldRejectByPFS,
                enquiryCompletion,
                "Updated",
                username,
                "EnquiryAction", "Enquiry Completion");

        return enquiryCompletion;
    }
}
