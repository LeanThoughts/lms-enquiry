package pfs.lms.enquiry.bmcapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc.BmcApprovalByICC;
import pfs.lms.enquiry.bmcapproval.bmcapprovalbyicc.BMCApprovalByICCRepository;
import pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay.BmcICCReasonForDelay;
import pfs.lms.enquiry.bmcapproval.bmciccreasonfordelay.BMCICCReasonForDelayRepository;
import pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer.BmcRejectedByCustomer;
import pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer.BMCRejectedByCustomerRepository;
import pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc.BMCRejectedByICC;
import pfs.lms.enquiry.bmcapproval.bmcrejectedbyicc.BMCRejectedByICCRepository;
import pfs.lms.enquiry.resource.WorkflowRequestResource;
import pfs.lms.enquiry.service.workflow.IWorkflowService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BMCICCApprovalController {

    private final BMCApprovalByICCRepository BMCApprovalByICCRepository;
    private final BMCRejectedByICCRepository BMCRejectedByICCRepository;
    private final BMCRejectedByCustomerRepository BMCRejectedByCustomerRepository;
    private final BMCICCReasonForDelayRepository BMCICCReasonForDelayRepository;
    private final IWorkflowService workflowService;

    @PostMapping("/api/bmcIccApprovals/sendForApproval")
    public ResponseEntity<Object> process(@RequestBody WorkflowRequestResource workflowRequestResource,
                                          HttpServletRequest request) throws Exception {

        BmcApprovalByICC BMCApprovalByICC =
                BMCApprovalByICCRepository.findByBmcICCApprovalId(workflowRequestResource.getBusinessProcessId());
        BMCRejectedByICC BMCRejectedByICC =
                BMCRejectedByICCRepository.findByBmcICCApprovalId(workflowRequestResource.getBusinessProcessId());
        BmcRejectedByCustomer BMCRejectedByCustomer =
                BMCRejectedByCustomerRepository.findByBmcICCApprovalId(workflowRequestResource.getBusinessProcessId());
        List<BmcICCReasonForDelay> BMCICCReasonForDelay =
                BMCICCReasonForDelayRepository.findByBmcICCApprovalId(workflowRequestResource.getBusinessProcessId());

        if (BMCApprovalByICC == null && BMCRejectedByICC == null && BMCRejectedByCustomer == null && BMCICCReasonForDelay == null)
            throw new Exception("Please record an entry in one of the following tabs, before sending for approval:\n" +
                    "- Rejected by ICC\n" +
                    "- ICC Approval\n" +
                    "- Rejected by Customer\n" +
                    "- Reason for Delay ");
        else if (BMCApprovalByICC != null) {
            String msg = "";
            if (BMCApprovalByICC.getMeetingNumber() == null || BMCApprovalByICC.getMeetingNumber().equals(""))
                msg = "ICC Approval Tab: Meeting number is empty. Please enter the value before sending for approval";
            else if (BMCApprovalByICC.getMeetingDate() == null)
                msg = "ICC Approval Tab: Meeting date is empty. Please enter the value before sending for approval";
            else if (BMCApprovalByICC.getCfoApprovalDate() == null)
                msg = "ICC Approval Tab: CFO approval date is empty. Please enter the value before sending for approval";
            else if (BMCApprovalByICC.getEdApprovalDate() == null)
                msg = "ICC Approval Tab: ED approval date is empty. Please enter the value before sending for approval";
            if (!msg.equals(""))
                throw new Exception(msg);
        }

        Object processObject = workflowService.startWorkflowProcessInstance(
                workflowRequestResource.getBusinessProcessId(),
                request.getUserPrincipal().getName(),
                workflowRequestResource.getRequestorEmail(),
                workflowRequestResource.getProcessName());

        return ResponseEntity.ok(processObject);
    }
}
