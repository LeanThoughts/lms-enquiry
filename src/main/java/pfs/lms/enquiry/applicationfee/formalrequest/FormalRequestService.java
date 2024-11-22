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
                    obj.setModified(true);
                    obj = applicationFeeRepository.save(obj);

                    // Change Documents for Application Fee - Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),
                            obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getEnquiryNo().getId().toString(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Application Fee", "Header");

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

        changeDocumentService.createChangeDocument(
                formalRequest.getId(),
                formalRequest.getId().toString(),
                formalRequest.getApplicationFee().getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                null,
                formalRequest,
                "Created",
                username,
                "Application Fee", "FormalRequest");

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

        ApplicationFee applicationFee = applicationFeeRepository.getOne(formalRequest.getApplicationFee().getId());
        applicationFee.setModified(true);
        applicationFeeRepository.save(applicationFee);

        // Change Documents
        changeDocumentService.createChangeDocument(
                formalRequest.getId(),
                formalRequest.getId().toString(),
                formalRequest.getApplicationFee().getId().toString(),
                formalRequest.getApplicationFee().getLoanApplication().getEnquiryNo().getId().toString(),
                oldFormalRequest,
                formalRequest,
                "Updated",
                username,
                "Application Fee", "FormalRequest");


        return formalRequest;
    }

    @Override
    public FormalRequest delete(UUID formalRequestId, String username) {
        FormalRequest formalRequest = formalRequestRepository.findById(formalRequestId)
                .orElseThrow(() -> new EntityNotFoundException(formalRequestId.toString()));

        ApplicationFee applicationFee = applicationFeeRepository.getOne(formalRequest.getApplicationFee().getId());
        applicationFee.setModified(true);
        applicationFeeRepository.save(applicationFee);

        formalRequestRepository.delete(formalRequest);

        changeDocumentService.createChangeDocument(
                formalRequest.getId(),
                formalRequest.getId().toString(),
                formalRequest.getApplicationFee().getId().toString(),
                formalRequest.getApplicationFee().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                formalRequest,
                "Deleted",
                username,
                "Application Fee", "FormalRequest");

        return formalRequest;
    }
}
