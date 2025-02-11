package pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc;

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
public class BMCRejectedByICCService implements IBMCRejectedByICCService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository bmcICCApprovalRepository;
    private final BMCRejectedByICCRepository bmcRejectedByICCRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BmcRejectedByIcc create(BMCRejectedByICCResource bmcRejectedByICCResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmcRejectedByICCResource.getLoanApplicationId());

        BmcIccApproval bmcICCApproval = bmcICCApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcIccApproval obj = new BmcIccApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmcICCApprovalRepository.save(obj);

                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "BmcApprovalByIcc", "Header");

                    return obj;
                });

        BmcRejectedByIcc BMCRejectedByIcc = new BmcRejectedByIcc();
        BMCRejectedByIcc.setBmcICCApproval(bmcICCApproval);
        BMCRejectedByIcc.setMeetingDate(bmcRejectedByICCResource.getMeetingDate());
        BMCRejectedByIcc.setMeetingNumber(bmcRejectedByICCResource.getMeetingNumber());
        BMCRejectedByIcc.setReasonForRejection(bmcRejectedByICCResource.getReasonForRejection());
        BMCRejectedByIcc = bmcRejectedByICCRepository.save(BMCRejectedByIcc);
        changeDocumentService.createChangeDocument(
                BMCRejectedByIcc.getId(),
                BMCRejectedByIcc.getId().toString(),
                BMCRejectedByIcc.getBmcICCApproval().getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                BMCRejectedByIcc,
                "Created",
                username,
                "BmcApprovalByIcc", "BMCRejectedByIcc");

        return BMCRejectedByIcc;
    }

    @Override
    public BmcRejectedByIcc update(BMCRejectedByICCResource bmcRejectedByICCResource, String username)
            throws CloneNotSupportedException {

        BmcRejectedByIcc BMCRejectedByIcc = bmcRejectedByICCRepository.findById(bmcRejectedByICCResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmcRejectedByICCResource.getId().toString()));

        Object oldBMCRejectedByIcc = BMCRejectedByIcc.clone();

        BMCRejectedByIcc.setMeetingDate(bmcRejectedByICCResource.getMeetingDate());
        BMCRejectedByIcc.setMeetingNumber(bmcRejectedByICCResource.getMeetingNumber());
        BMCRejectedByIcc.setReasonForRejection(bmcRejectedByICCResource.getReasonForRejection());
        BMCRejectedByIcc = bmcRejectedByICCRepository.save(BMCRejectedByIcc);

        BmcIccApproval bmcICCApproval = bmcICCApprovalRepository.getOne(BMCRejectedByIcc.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcICCApproval = bmcICCApprovalRepository.save(bmcICCApproval);

        changeDocumentService.createChangeDocument(
                BMCRejectedByIcc.getId(),
                BMCRejectedByIcc.getId().toString(),
                BMCRejectedByIcc.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                oldBMCRejectedByIcc,
                BMCRejectedByIcc,
                "Updated",
                username,
                "BmcApprovalByIcc", "BMCRejectedByIcc");

        return BMCRejectedByIcc;
    }

    @Override
    public BmcRejectedByIcc delete(UUID rejectedByICCId, String username) {
        BmcRejectedByIcc BMCRejectedByIcc = bmcRejectedByICCRepository.findById(rejectedByICCId)
                .orElseThrow(() -> new EntityNotFoundException(rejectedByICCId.toString()));

        BmcIccApproval bmcICCApproval = bmcICCApprovalRepository.getOne(BMCRejectedByIcc.getBmcICCApproval().getId());
        bmcICCApproval.setModified(true);
        bmcICCApprovalRepository.save(bmcICCApproval);

        bmcRejectedByICCRepository.delete(BMCRejectedByIcc);

        changeDocumentService.createChangeDocument(
                BMCRejectedByIcc.getId(),
                BMCRejectedByIcc.getId().toString(),
                BMCRejectedByIcc.getBmcICCApproval().getId().toString(),
                bmcICCApproval.getLoanApplication().getLoanContractId(),
                null,
                BMCRejectedByIcc,
                "Deleted",
                username,
                "BmcApprovalByIcc", "BMCRejectedByIcc");
        return BMCRejectedByIcc;
    }
}
