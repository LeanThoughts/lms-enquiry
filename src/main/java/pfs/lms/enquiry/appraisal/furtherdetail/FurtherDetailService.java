package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FurtherDetailService implements IFurtherDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final FurtherDetailRepository furtherDetailRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public FurtherDetail updateFurtherDetails(FurtherDetailResource furtherDetailResource, String username) throws CloneNotSupportedException {
        LoanApplication loanApplication = loanApplicationRepository.getOne(furtherDetailResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),
                            obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");
                    return obj;
                });
        FurtherDetail furtherDetail = furtherDetailRepository.findByLoanAppraisalId(loanAppraisal.getId())
                .orElseGet(FurtherDetail::new);

        Object oldFurtherDetail =   furtherDetail.clone();

        furtherDetail.setDate(furtherDetailResource.getDate());
        furtherDetail.setFurtherDetails(furtherDetailResource.getFurtherDetails());
        if (furtherDetail.getLoanAppraisal() == null)
            furtherDetail.setLoanAppraisal(loanAppraisal);
        furtherDetail = furtherDetailRepository.save(furtherDetail);

        // Change Documents for Further Details
        changeDocumentService.createChangeDocument(
                furtherDetail.getLoanAppraisal().getId(),
                furtherDetail.getId().toString(),
                furtherDetail.getLoanAppraisal().getId().toString(),
                furtherDetail.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldFurtherDetail,
                furtherDetail,
                "Updated",
                username,
                "Appraisal", "Further Detail" );

        return furtherDetail;
    }
}
