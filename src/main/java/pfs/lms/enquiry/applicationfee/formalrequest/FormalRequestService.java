package pfs.lms.enquiry.applicationfee.formalrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FormalRequestService implements IFormalRequestService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationFeeRepository applicationFeeRepository;
    private final FormalRequestRepository formalRequestRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public FormalRequest create(FormalRequestResource formalRequestResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(formalRequestResource.getLoanApplicationId());

        ApplicationFee applicationFee = applicationFeeRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ApplicationFee obj = new ApplicationFee();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = applicationFeeRepository.save(obj);

                    // Change Documents for Appraisal Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "Appraisal", "Header");

                    return obj;
                });

        FormalRequest formalRequest = new FormalRequest();
        formalRequest.setApplicationFee(applicationFee);
        formalRequest.setSerialNumber(formalRequestRepository.findByApplicationFeeIdOrderBySerialNumber(applicationFee.getId()).size() + 1);
        formalRequest.setDocumentName(formalRequestResource.getDocumentName());
        formalRequest.setUploadDate(formalRequestResource.getUploadDate());
        formalRequest.setDocumentLetterDate(formalRequestResource.getDocumentLetterDate());
        formalRequest.setDocumentReceivedDate(formalRequestResource.getDocumentReceivedDate());
        formalRequest.setFileReference(formalRequestResource.getFileReference());
        formalRequest = formalRequestRepository.save(formalRequest);
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanApplication.getLoanContractId(),
//                null,
//                loanPartner,
//                "Created",
//                username,
//                "Appraisal", "Loan Partner");

        return formalRequest;
    }

    @Override
    public FormalRequest update(FormalRequestResource formalRequestResource, String username)
            throws CloneNotSupportedException {

        FormalRequest formalRequest = formalRequestRepository.findById(formalRequestResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(formalRequestResource.getId().toString()));

        Object oldFormalRequest = formalRequest.clone();

        formalRequest.setDocumentName(formalRequestResource.getDocumentName());
        formalRequest.setUploadDate(formalRequestResource.getUploadDate());
        formalRequest.setDocumentLetterDate(formalRequestResource.getDocumentLetterDate());
        formalRequest.setDocumentReceivedDate(formalRequestResource.getDocumentReceivedDate());
        formalRequest.setFileReference(formalRequestResource.getFileReference());
        formalRequest = formalRequestRepository.save(formalRequest);

        // Change Documents for  Loan Partner
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanPartner.getLoanApplication().getLoanContractId(),
//                oldLoanPartner,
//                loanPartner,
//                "Updated",
//                username,
//                "Appraisal", "Loan Partner");

        return formalRequest;
    }

    @Override
    public FormalRequest delete(UUID formalRequestId, String username) {
        FormalRequest formalRequest = formalRequestRepository.findById(formalRequestId)
                .orElseThrow(() -> new EntityNotFoundException(formalRequestId.toString()));
        formalRequestRepository.delete(formalRequest);
        return formalRequest;
    }
}
