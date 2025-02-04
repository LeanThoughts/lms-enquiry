package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BmcICCApproval;
import pfs.lms.enquiry.bmcapproval.BMCICCApprovalRepository;
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
public class BMCICCFurtherDetailService implements IBMCICCFurtherDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository bmciccApprovalRepository;
    private final BMCICCFurtherDetailRepository bmciccFurtherDetailRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BmcICCFurtherDetail create(BMCICCFurtherDetailResource bmciccFurtherDetailResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmciccFurtherDetailResource.getLoanApplicationId());

        BmcICCApproval BMCICCApproval = bmciccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcICCApproval obj = new BmcICCApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmciccApprovalRepository.save(obj);

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

        BmcICCFurtherDetail bmciccFurtherDetail = new BmcICCFurtherDetail();
        bmciccFurtherDetail.setBmcICCApproval(BMCICCApproval);
        bmciccFurtherDetail.setSerialNumber(bmciccFurtherDetailRepository.findByBmcICCApprovalId(BMCICCApproval.getId()).size() + 1);
        bmciccFurtherDetail.setDetailsRequired(bmciccFurtherDetailResource.getDetailsRequired());
        bmciccFurtherDetail.setIccMeetingNumber(bmciccFurtherDetailResource.getIccMeetingNumber());
        bmciccFurtherDetail.setIccMeetingDate(bmciccFurtherDetailResource.getIccMeetingDate());
        bmciccFurtherDetail = bmciccFurtherDetailRepository.save(bmciccFurtherDetail);
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

        return bmciccFurtherDetail;
    }

    @Override
    public BmcICCFurtherDetail update(BMCICCFurtherDetailResource bmciccFurtherDetailResource, String username)
            throws CloneNotSupportedException {

        BmcICCFurtherDetail bmciccFurtherDetail = bmciccFurtherDetailRepository.findById(bmciccFurtherDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmciccFurtherDetailResource.getId().toString()));

        Object oldICCFurtherDetail = bmciccFurtherDetail.clone();

        bmciccFurtherDetail.setIccMeetingDate(bmciccFurtherDetailResource.getIccMeetingDate());
        bmciccFurtherDetail.setIccMeetingNumber(bmciccFurtherDetailResource.getIccMeetingNumber());
        bmciccFurtherDetail.setDetailsRequired(bmciccFurtherDetailResource.getDetailsRequired());
        bmciccFurtherDetail = bmciccFurtherDetailRepository.save(bmciccFurtherDetail);

        BmcICCApproval BMCICCApproval = bmciccApprovalRepository.getOne(bmciccFurtherDetail.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        bmciccApprovalRepository.save(BMCICCApproval);

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

        return bmciccFurtherDetail;
    }

    @Override
    public BmcICCFurtherDetail delete(UUID bmciccFurtherDetailId, String username) {
        BmcICCFurtherDetail bmciccFurtherDetail = bmciccFurtherDetailRepository.findById(bmciccFurtherDetailId)
                .orElseThrow(() -> new EntityNotFoundException(bmciccFurtherDetailId.toString()));
        
        BmcICCApproval bmcICCApproval = bmciccApprovalRepository.getOne(bmciccFurtherDetail.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmciccApprovalRepository.save(bmcICCApproval);

        bmciccFurtherDetailRepository.delete(bmciccFurtherDetail);
        
        return bmciccFurtherDetail;
    }
}
