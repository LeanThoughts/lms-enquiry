package pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc;

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
public class BMCApprovalByICCService implements IBMCApprovalByICCService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final BMCICCApprovalRepository bmcICCApprovalRepository;
    private final BMCApprovalByICCRepository bmcApprovalByIccRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public BmcApprovalByIcc create(BMCApprovalByICCResource bmcApprovalByICCResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(bmcApprovalByICCResource.getLoanApplicationId());

        BmcIccApproval BMCICCApproval = bmcICCApprovalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    BmcIccApproval obj = new BmcIccApproval();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj.setModified(true);
                    obj = bmcICCApprovalRepository.save(obj);

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

        BmcApprovalByIcc BMCApprovalByIcc = new BmcApprovalByIcc();
        BMCApprovalByIcc.setBmcICCApproval(BMCICCApproval);
        BMCApprovalByIcc.setMeetingDate(bmcApprovalByICCResource.getMeetingDate());
        BMCApprovalByIcc.setMeetingNumber(bmcApprovalByICCResource.getMeetingNumber());
        BMCApprovalByIcc.setEdApprovalDate(bmcApprovalByICCResource.getEdApprovalDate());
        BMCApprovalByIcc.setCfoApprovalDate(bmcApprovalByICCResource.getCfoApprovalDate());
        BMCApprovalByIcc.setRemarks(bmcApprovalByICCResource.getRemarks());
        BMCApprovalByIcc.setFileReference1(bmcApprovalByICCResource.getFileReference1());
        BMCApprovalByIcc.setFileReference2(bmcApprovalByICCResource.getFileReference2());
        BMCApprovalByIcc.setDocumentTypeMinutes(bmcApprovalByICCResource.getDocumentTypeMinutes());
        BMCApprovalByIcc.setDocumentTypeMailFromCS(bmcApprovalByICCResource.getDocumentTypeMailFromCS());
        BMCApprovalByIcc = bmcApprovalByIccRepository.save(BMCApprovalByIcc);
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

        return BMCApprovalByIcc;
    }

    @Override
    public BmcApprovalByIcc update(BMCApprovalByICCResource bmcApprovalByICCResource, String username)
            throws CloneNotSupportedException {

        BmcApprovalByIcc BMCApprovalByIcc = bmcApprovalByIccRepository.findById(bmcApprovalByICCResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmcApprovalByICCResource.getId().toString()));

        Object oldICCFurtherDetail = BMCApprovalByIcc.clone();

        BMCApprovalByIcc.setMeetingDate(bmcApprovalByICCResource.getMeetingDate());
        BMCApprovalByIcc.setMeetingNumber(bmcApprovalByICCResource.getMeetingNumber());
        BMCApprovalByIcc.setRemarks(bmcApprovalByICCResource.getRemarks());
        BMCApprovalByIcc.setEdApprovalDate(bmcApprovalByICCResource.getEdApprovalDate());
        BMCApprovalByIcc.setCfoApprovalDate(bmcApprovalByICCResource.getCfoApprovalDate());
        BMCApprovalByIcc.setFileReference1(bmcApprovalByICCResource.getFileReference1());
        BMCApprovalByIcc.setFileReference2(bmcApprovalByICCResource.getFileReference2());
        BMCApprovalByIcc.setDocumentTypeMinutes(bmcApprovalByICCResource.getDocumentTypeMinutes());
        BMCApprovalByIcc.setDocumentTypeMailFromCS(bmcApprovalByICCResource.getDocumentTypeMailFromCS());

        BMCApprovalByIcc = bmcApprovalByIccRepository.save(BMCApprovalByIcc);

        BmcIccApproval BMCICCApproval = bmcICCApprovalRepository.getOne(BMCApprovalByIcc.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        bmcICCApprovalRepository.save(BMCICCApproval);

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

        return BMCApprovalByIcc;
    }

    @Override
    public BmcApprovalByIcc delete(UUID bmcApprovalByICCId, String username) {
        BmcApprovalByIcc BMCApprovalByIcc = bmcApprovalByIccRepository.findById(bmcApprovalByICCId)
                .orElseThrow(() -> new EntityNotFoundException(bmcApprovalByICCId.toString()));
        
        BmcIccApproval BMCICCApproval = bmcICCApprovalRepository.getOne(BMCApprovalByIcc.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        bmcICCApprovalRepository.save(BMCICCApproval);

        bmcApprovalByIccRepository.delete(BMCApprovalByIcc);
        return BMCApprovalByIcc;
    }
}
