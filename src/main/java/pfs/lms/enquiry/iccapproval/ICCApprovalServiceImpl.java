package pfs.lms.enquiry.iccapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICC;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICCRepository;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetail;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetailRepository;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelay;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelayRepository;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomer;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomerRepository;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICC;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICCRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ICCApprovalServiceImpl implements ICCApprovalService {

    private  final LoanApplicationRepository loanApplicationRepository;
    private final IChangeDocumentService changeDocumentService;
    private final ICCApprovalRepository iccApprovalRepository;

    private final ICCFurtherDetailRepository iccFurtherDetailRepository;
    private final ICCReasonForDelayRepository iccReasonForDelayRepository;
    private final RejectedByICCRepository rejectedByICCRepository;
    private final ApprovalByICCRepository approvalByICCRepository;
    private final RejectedByCustomerRepository rejectedByCustomerRepository;

    @Override
    public ICCApproval create(ICCApproval iccApproval, String username) throws Exception {
        return null;
    }

    @Override
    public ICCApproval update(ICCApproval iccApproval, String username) throws Exception {
        return null;
    }

    @Override
    public ICCApproval processRejection(ICCApproval iccApproval, String username) throws CloneNotSupportedException {
        Object oldIccApproval = iccApproval.clone();
        iccApproval.setWorkFlowStatusCode(04);
        iccApproval.setWorkFlowStatusDescription("Rejected");

        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                iccApproval.getId(), iccApproval.getId().toString(), null,
                iccApproval.getLoanApplication().getLoanContractId(),
                iccApproval,
                oldIccApproval,
                "Updated",
                username,
                "ICC In-Principal Approval", "Header");
        iccApprovalRepository.save(iccApproval);

        return iccApproval;
    }

    @Override
    public ICCApproval processApprovedICC(ICCApproval iccApproval, String username) throws CloneNotSupportedException {

        LoanApplication loanApplication = iccApproval.getLoanApplication();
        Object oldLoanApplication;
        oldLoanApplication = loanApplication.clone();


        loanApplication.setFunctionalStatus(2);
        loanApplication.setFunctionalStatusDescription("ICC In-Principle Approved");

        // Set icc related information
        ArrayList<ICCApprovalEventLog> eventLogs = new ArrayList<>();

        List<ICCFurtherDetail> furtherDetails = iccFurtherDetailRepository.findByIccApprovalId(iccApproval.getId());
        if (furtherDetails != null && furtherDetails.size() > 0) {
            furtherDetails.forEach(furtherDetail -> {
                ICCApprovalEventLog iccApprovalEventLog = new ICCApprovalEventLog();
                iccApprovalEventLog.setIccApproval(iccApproval);
                iccApprovalEventLog.setEventType("Further details requested by ICC");
                iccApprovalEventLog.setEventDate(furtherDetail.getIccMeetingDate());
                iccApprovalEventLog.setMeetingNumber(furtherDetail.getIccMeetingNumber());
                iccApprovalEventLog.setRemarks(furtherDetail.getDetailsRequired());
                eventLogs.add(iccApprovalEventLog);
            });
        }

        List<ICCReasonForDelay> reasonForDelays = iccReasonForDelayRepository.findByIccApprovalId(iccApproval.getId());
        if (reasonForDelays != null && reasonForDelays.size() > 0) {
            reasonForDelays.forEach(reasonForDelay -> {
                ICCApprovalEventLog iccApprovalEventLog = new ICCApprovalEventLog();
                iccApprovalEventLog.setIccApproval(iccApproval);
                iccApprovalEventLog.setEventType("On Hold");
                iccApprovalEventLog.setEventDate(reasonForDelay.getDate());
                iccApprovalEventLog.setRemarks(reasonForDelay.getReasonForDelay());
                eventLogs.add(iccApprovalEventLog);
            });
        }

        RejectedByICC rejectedByICC = rejectedByICCRepository.findByIccApprovalId(iccApproval.getId());
        if (rejectedByICC != null) {
            ICCApprovalEventLog iccApprovalEventLog = new ICCApprovalEventLog();
            iccApprovalEventLog.setIccApproval(iccApproval);
            iccApprovalEventLog.setEventType("Rejected by ICC");
            iccApprovalEventLog.setEventDate(rejectedByICC.getMeetingDate());
            iccApprovalEventLog.setMeetingNumber(rejectedByICC.getMeetingNumber());
            iccApprovalEventLog.setRemarks(rejectedByICC.getReasonForRejection());
            eventLogs.add(iccApprovalEventLog);
        }

        ApprovalByICC approvalByICC = approvalByICCRepository.findByIccApprovalId(iccApproval.getId());
        if (approvalByICC != null) {
            ICCApprovalEventLog iccApprovalEventLog = new ICCApprovalEventLog();
            iccApprovalEventLog.setIccApproval(iccApproval);
            iccApprovalEventLog.setEventType("Approved by ICC");
            iccApprovalEventLog.setEventDate(approvalByICC.getMeetingDate());
            iccApprovalEventLog.setMeetingNumber(approvalByICC.getMeetingNumber());
            iccApprovalEventLog.setRemarks(approvalByICC.getRemarks());
            loanApplication.setAmountApproved(approvalByICC.getAmountApproved());
            loanApplication.setIccApprovedRoi(approvalByICC.getIccApprovedRoi());
            eventLogs.add(iccApprovalEventLog);
        }

        RejectedByCustomer rejectedByCustomer = rejectedByCustomerRepository.findByIccApprovalId(iccApproval.getId());
        if (rejectedByCustomer != null) {
            ICCApprovalEventLog iccApprovalEventLog = new ICCApprovalEventLog();
            iccApprovalEventLog.setIccApproval(iccApproval);
            iccApprovalEventLog.setEventType("Rejected by Customer");
            iccApprovalEventLog.setEventDate(rejectedByCustomer.getDateOfRejection());
            iccApprovalEventLog.setMeetingNumber(rejectedByCustomer.getMeetingNumber());
            iccApprovalEventLog.setRemarks(rejectedByCustomer.getRemarks());
            eventLogs.add(iccApprovalEventLog);
        }

        if (eventLogs.size() > 0) {
            ICCApprovalEventLog latestLog = eventLogs.stream()
                    .max(Comparator.comparing(ICCApprovalEventLog::getEventDate)).get();
            loanApplication.setiCCMeetNumber(latestLog.getMeetingNumber());
            loanApplication.setBoardMeetingNumber(latestLog.getMeetingNumber());
            loanApplication.setiCCRemarks(latestLog.getRemarks());
            loanApplication.setiCCStatus(latestLog.getEventType());
            loanApplication.setiCCClearanceDate(latestLog.getEventDate());
        }

        // Change Documents for Loan Application
        changeDocumentService.createChangeDocument(
                loanApplication.getId(),
                loanApplication.getId().toString(),
                loanApplication.getId().toString(),
                loanApplication.getEnquiryNo().getId().toString(),
                loanApplication,
                oldLoanApplication,
                "Updated",
                username,
                "LoanApplication", "LoanApplication" );

        loanApplicationRepository.save(loanApplication);

        return iccApproval;
    }
}
