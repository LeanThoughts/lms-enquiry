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

        changeDocumentService.createChangeDocument(
                BMCApprovalByIcc.getId(),
                BMCApprovalByIcc.getId().toString(),
                BMCApprovalByIcc.getBmcICCApproval().getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                BMCApprovalByIcc,
                "Created",
                username,
                "BmcApprovalByIcc", "BmcApprovalByIcc");

        return BMCApprovalByIcc;
    }

    @Override
    public BmcApprovalByIcc update(BMCApprovalByICCResource bmcApprovalByICCResource, String username)
            throws CloneNotSupportedException {

        BmcApprovalByIcc BMCApprovalByIcc = bmcApprovalByIccRepository.findById(bmcApprovalByICCResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(bmcApprovalByICCResource.getId().toString()));

        Object oldBmcApprovalByIcc = BMCApprovalByIcc.clone();

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
        BMCICCApproval =  bmcICCApprovalRepository.save(BMCICCApproval);

        changeDocumentService.createChangeDocument(
                BMCICCApproval.getId(),
                BMCICCApproval.getId().toString(),
                BMCApprovalByIcc.getBmcICCApproval().getId().toString(),
                BMCICCApproval.getLoanApplication().getLoanContractId(),
                oldBmcApprovalByIcc,
                BMCICCApproval,
                "Updated",
                username,
                "BmcApprovalByIcc", "BmcApprovalByIcc");

        return BMCApprovalByIcc;
    }

    @Override
    public BmcApprovalByIcc delete(UUID bmcApprovalByICCId, String username) {
        BmcApprovalByIcc BMCApprovalByIcc = bmcApprovalByIccRepository.findById(bmcApprovalByICCId)
                .orElseThrow(() -> new EntityNotFoundException(bmcApprovalByICCId.toString()));
        
        BmcIccApproval BMCICCApproval = bmcICCApprovalRepository.getOne(BMCApprovalByIcc.getBmcICCApproval().getId());
        BMCICCApproval.setModified(true);
        LoanApplication loanApplication = BMCICCApproval.getLoanApplication();
        bmcICCApprovalRepository.save(BMCICCApproval);

        bmcApprovalByIccRepository.delete(BMCApprovalByIcc);

        changeDocumentService.createChangeDocument(
                BMCApprovalByIcc.getId(),
                BMCApprovalByIcc.getId().toString(),
                BMCApprovalByIcc.getBmcICCApproval().getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                BMCApprovalByIcc,
                "Deleted",
                username,
                "BmcApprovalByIcc", "BmcApprovalByIcc");
        return BMCApprovalByIcc;
    }
//
//    @Override
//    public BMCICCApproval processApprovedICC(BmcIccApproval BMCICCApproval, String username) throws CloneNotSupportedException {
//
//        LoanApplication loanApplication = BMCICCApproval.getLoanApplication();
//        Object oldLoanApplication;
//        oldLoanApplication = loanApplication.clone();
//
//
//        loanApplication.setFunctionalStatus(12);
//        loanApplication.setFunctionalStatusDescription("BMC Approval");
//
//        // Change Documents for Loan Application
//        changeDocumentService.createChangeDocument(
//                loanApplication.getId(),
//                loanApplication.getId().toString(),
//                loanApplication.getId().toString(),
//                loanApplication.getEnquiryNo().getId().toString(),
//                loanApplication,
//                oldLoanApplication,
//                "Updated",
//                username,
//                "LoanApplication", "LoanApplication" );
//        loanApplication.setPostedInSAP(0);
//
//        loanApplicationRepository.save(loanApplication);
//
//        return BMCICCApproval;
//    }

//    @Override
//    public BmcApprovalByIcc processRejection(BmcApprovalByIcc bmcApprovalByIcc, String username) throws CloneNotSupportedException {
//
//        BmcIccApproval bmcIccApproval = bmcApprovalByIcc.getBmcICCApproval();
//        Object oldBmcIccApproval = bmcIccApproval.clone();
//        bmcIccApproval.setWorkFlowStatusCode(04);
//        bmcIccApproval.setWorkFlowStatusDescription("Rejected");
//
//        // Change Documents for Monitoring Header
//        changeDocumentService.createChangeDocument(
//                bmcIccApproval.getId(), bmcIccApproval.getId().toString(), null,
//                bmcIccApproval.getLoanApplication().getLoanContractId(),
//                oldBmcIccApproval,
//                bmcIccApproval,
//                "Updated",
//                username,
//                "BmcApprovalByIcc Approval", "Header");
//        bmcICCApprovalRepository.save(bmcIccApproval);
//
//        return bmcApprovalByIcc;
//    }
}
