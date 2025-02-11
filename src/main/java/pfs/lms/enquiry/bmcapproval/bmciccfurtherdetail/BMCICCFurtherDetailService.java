package pfs.lms.enquiry.bmcapproval.bmciccfurtherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.bmcapproval.BmcIccApproval;
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
    public BmcIccFurtherDetail create(BMCICCFurtherDetailResource bmciccFurtherDetailResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmciccFurtherDetailResource.getLoanApplicationId());

        BmcIccApproval BMCICCApproval = bmciccApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcIccApproval obj = new BmcIccApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmciccApprovalRepository.save(obj);

                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "BmcIccApproval", "Header");
                    return obj;
                });

        BmcIccFurtherDetail bmciccFurtherDetail = new BmcIccFurtherDetail();
        bmciccFurtherDetail.setBmcICCApproval(BMCICCApproval);
        bmciccFurtherDetail.setSerialNumber(bmciccFurtherDetailRepository.findByBmcICCApprovalId(BMCICCApproval.getId()).size() + 1);
        bmciccFurtherDetail.setDetailsRequired(bmciccFurtherDetailResource.getDetailsRequired());
        bmciccFurtherDetail.setIccMeetingNumber(bmciccFurtherDetailResource.getIccMeetingNumber());
        bmciccFurtherDetail.setIccMeetingDate(bmciccFurtherDetailResource.getIccMeetingDate());
        bmciccFurtherDetail = bmciccFurtherDetailRepository.save(bmciccFurtherDetail);

        changeDocumentService.createChangeDocument(
                bmciccFurtherDetail.getId(),
                bmciccFurtherDetail.getId().toString(),
                bmciccFurtherDetail.getBmcICCApproval().getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                bmciccFurtherDetail,
                "Created",
                username,
                "BmcApprovalByIcc", "BmcIccFurtherDetail");

        return bmciccFurtherDetail;
    }

    @Override
    public BmcIccFurtherDetail update(BMCICCFurtherDetailResource bmciccFurtherDetailResource, String username)
            throws CloneNotSupportedException {

        BmcIccFurtherDetail bmciccFurtherDetail = bmciccFurtherDetailRepository.findById(bmciccFurtherDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmciccFurtherDetailResource.getId().toString()));

        Object oldBmciccFurtherDetail = bmciccFurtherDetail.clone();

        bmciccFurtherDetail.setIccMeetingDate(bmciccFurtherDetailResource.getIccMeetingDate());
        bmciccFurtherDetail.setIccMeetingNumber(bmciccFurtherDetailResource.getIccMeetingNumber());
        bmciccFurtherDetail.setDetailsRequired(bmciccFurtherDetailResource.getDetailsRequired());
        bmciccFurtherDetail = bmciccFurtherDetailRepository.save(bmciccFurtherDetail);

        BmcIccApproval BMCICCApproval = bmciccApprovalRepository.getOne(bmciccFurtherDetail.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        bmciccApprovalRepository.save(BMCICCApproval);

        changeDocumentService.createChangeDocument(
                bmciccFurtherDetail.getId(),
                bmciccFurtherDetail.getId().toString(),
                bmciccFurtherDetail.getBmcICCApproval().getId().toString(),
                BMCICCApproval.getLoanApplication().getLoanContractId(),
                oldBmciccFurtherDetail,
                bmciccFurtherDetail,
                "Updated",
                username,
                "BmcApprovalByIcc", "BmcIccFurtherDetail");

        return bmciccFurtherDetail;
    }

    @Override
    public BmcIccFurtherDetail delete(UUID bmciccFurtherDetailId, String username) {
        BmcIccFurtherDetail bmciccFurtherDetail = bmciccFurtherDetailRepository.findById(bmciccFurtherDetailId)
                .orElseThrow(() -> new EntityNotFoundException(bmciccFurtherDetailId.toString()));
        
        BmcIccApproval bmcICCApproval = bmciccApprovalRepository.getOne(bmciccFurtherDetail.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmciccApprovalRepository.save(bmcICCApproval);

        bmciccFurtherDetailRepository.delete(bmciccFurtherDetail);

        changeDocumentService.createChangeDocument(
                bmciccFurtherDetail.getId(),
                bmciccFurtherDetail.getId().toString(),
                bmciccFurtherDetail.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                null,
                bmciccFurtherDetail,
                "Deleted",
                username,
                "BmcApprovalByIcc", "BmcIccFurtherDetail");

        return bmciccFurtherDetail;
    }
}
