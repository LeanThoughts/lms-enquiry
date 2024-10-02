package pfs.lms.enquiry.iccapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICC;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICCRepository;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelay;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelayRepository;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomer;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomerRepository;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICC;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICCRepository;
import pfs.lms.enquiry.resource.WorkflowRequestResource;
import pfs.lms.enquiry.service.workflow.IWorkflowService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ICCApprovalController {

    private final ApprovalByICCRepository approvalByICCRepository;
    private final RejectedByICCRepository rejectedByICCRepository;
    private final RejectedByCustomerRepository rejectedByCustomerRepository;
    private final ICCReasonForDelayRepository iccReasonForDelayRepository;
    private final IWorkflowService workflowService;

    @PostMapping("/api/iccApprovals/sendForApproval")
    public ResponseEntity<Object> process(@RequestBody WorkflowRequestResource workflowRequestResource,
                                          HttpServletRequest request) throws Exception {

        ApprovalByICC approvalByICC =
                approvalByICCRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());
        RejectedByICC rejectedByICC =
                rejectedByICCRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());
        RejectedByCustomer rejectedByCustomer =
                rejectedByCustomerRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());
        ICCReasonForDelay iccReasonForDelay =
                iccReasonForDelayRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());

        if (approvalByICC == null && rejectedByICC == null && rejectedByCustomer == null && iccReasonForDelay == null)
            throw new Exception("Please record an entry in one of the following tabs, before sending for approval:\n" +
                    "- Rejected by ICC\n" +
                    "- ICC Approval\n" +
                    "- Rejected by Customer\n" +
                    "- Reason for Delay ");
        else if (approvalByICC != null) {
            String msg = "";
            if (approvalByICC.getMeetingNumber() == null || approvalByICC.getMeetingNumber().equals(""))
                msg = "ICC Approval Tab: Meeting number is empty. Please enter the value before sending for approval";
            else if (approvalByICC.getMeetingDate() == null)
                msg = "ICC Approval Tab: Meeting date is empty. Please enter the value before sending for approval";
            else if (approvalByICC.getCfoApprovalDate() == null)
                msg = "ICC Approval Tab: CFO approval date is empty. Please enter the value before sending for approval";
            else if (approvalByICC.getEdApprovalDate() == null)
                msg = "ICC Approval Tab: ED approval date is empty. Please enter the value before sending for approval";
            if (!msg.equals(""))
                throw new Exception(msg);
        }

        Object processObject = workflowService.startWorkflowProcessInstance(
                workflowRequestResource.getBusinessProcessId(),
                request.getUserPrincipal().getName(),
                workflowRequestResource.getRequestorEmail(),
                workflowRequestResource.getProcessName());

        return ResponseEntity.ok(null);
    }
}
