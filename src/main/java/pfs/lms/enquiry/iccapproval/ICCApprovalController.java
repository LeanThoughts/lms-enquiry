package pfs.lms.enquiry.iccapproval;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICC;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICCRepository;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelay;
import pfs.lms.enquiry.iccapproval.iccreasonfordelay.ICCReasonForDelayRepository;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomer;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomerRepository;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICC;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICCRepository;
import pfs.lms.enquiry.resource.WorkflowRequestResource;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ICCApprovalController {

    private final ApprovalByICCRepository approvalByICCRepository;
    private final RejectedByICCRepository rejectedByICCRepository;
    private final RejectedByCustomerRepository rejectedByCustomerRepository;
    private final ICCReasonForDelayRepository iccReasonForDelayRepository;

    @PutMapping("/iccApprovals/sendForApproval")
    public ResponseEntity.BodyBuilder create(@RequestBody WorkflowRequestResource workflowRequestResource,
                                             HttpServletRequest request) {

        ApprovalByICC approvalByICC =
                approvalByICCRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());
        RejectedByICC rejectedByICC =
                rejectedByICCRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());
        RejectedByCustomer rejectedByCustomer =
                rejectedByCustomerRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());
        ICCReasonForDelay iccReasonForDelay =
                iccReasonForDelayRepository.findByIccApprovalId(workflowRequestResource.getBusinessProcessId());

        return ResponseEntity.ok();
    }
}
