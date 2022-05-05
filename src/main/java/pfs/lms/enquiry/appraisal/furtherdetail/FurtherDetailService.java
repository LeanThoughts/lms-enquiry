package pfs.lms.enquiry.appraisal.furtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class FurtherDetailService implements IFurtherDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final FurtherDetailRepository furtherDetailRepository;

    @Override
    public FurtherDetail updateFurtherDetails(FurtherDetailResource furtherDetailResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(furtherDetailResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        FurtherDetail furtherDetail = furtherDetailRepository.findByLoanAppraisalId(loanAppraisal.getId())
                .orElseGet(FurtherDetail::new);
        furtherDetail.setDate(furtherDetailResource.getDate());
        furtherDetail.setFurtherDetails(furtherDetailResource.getFurtherDetails());
        if (furtherDetail.getLoanAppraisal() == null)
            furtherDetail.setLoanAppraisal(loanAppraisal);
        furtherDetail = furtherDetailRepository.save(furtherDetail);
        return furtherDetail;
    }
}
