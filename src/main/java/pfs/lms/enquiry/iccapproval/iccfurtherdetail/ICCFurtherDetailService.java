package pfs.lms.enquiry.iccapproval.iccfurtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ICCFurtherDetailService implements IICCFurtherDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ICCApprovalRepository iccApprovalRepository;
    private final ICCFurtherDetailRepository iccFurtherDetailRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ICCFurtherDetail create(ICCFurtherDetailResource iccFurtherDetailResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(iccFurtherDetailResource.getLoanApplicationId());

        ICCApproval iccApproval = iccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ICCApproval obj = new ICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = iccApprovalRepository.save(obj);

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

        ICCFurtherDetail iccFurtherDetail = new ICCFurtherDetail();
        iccFurtherDetail.setIccApproval(iccApproval);
        iccFurtherDetail.setSerialNumber(iccFurtherDetailRepository.findByIccApprovalId(iccApproval.getId()).size() + 1);
        iccFurtherDetail.setDetailsRequired(iccFurtherDetailResource.getDetailsRequired());
        iccFurtherDetail.setIccMeetingNumber(iccFurtherDetailResource.getIccMeetingNumber());
        iccFurtherDetail.setIccMeetingDate(iccFurtherDetailResource.getIccMeetingDate());
        iccFurtherDetail = iccFurtherDetailRepository.save(iccFurtherDetail);
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

        return iccFurtherDetail;
    }

    @Override
    public ICCFurtherDetail update(ICCFurtherDetailResource iccFurtherDetailResource, String username)
            throws CloneNotSupportedException {

        ICCFurtherDetail iccFurtherDetail = iccFurtherDetailRepository.findById(iccFurtherDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(iccFurtherDetailResource.getId().toString()));

        Object oldICCFurtherDetail = iccFurtherDetail.clone();

        iccFurtherDetail.setIccMeetingDate(iccFurtherDetailResource.getIccMeetingDate());
        iccFurtherDetail.setIccMeetingNumber(iccFurtherDetailResource.getIccMeetingNumber());
        iccFurtherDetail.setDetailsRequired(iccFurtherDetailResource.getDetailsRequired());
        iccFurtherDetail = iccFurtherDetailRepository.save(iccFurtherDetail);

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

        return iccFurtherDetail;
    }

    @Override
    public ICCFurtherDetail delete(UUID iccFurtherDetailId, String username) {
        ICCFurtherDetail iccFurtherDetail = iccFurtherDetailRepository.findById(iccFurtherDetailId)
                .orElseThrow(() -> new EntityNotFoundException(iccFurtherDetailId.toString()));
        iccFurtherDetailRepository.delete(iccFurtherDetail);
        return iccFurtherDetail;
    }
}
