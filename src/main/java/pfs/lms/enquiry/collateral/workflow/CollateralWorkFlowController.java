package pfs.lms.enquiry.collateral.workflow;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.collateral.dto.CollateralChecklistDto;
import pfs.lms.enquiry.collateral.service.ICollateralAuthorizationService;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Approval workflow of collateral checklists, like the existing WorkflowController (startprocess, approvetask,
 * rejecttask) but for process {@value ICollateralWorkflowService#PROCESS_NAME}. Paths sit under /api.
 * The approver's open tasks are listed by the existing /api/tasklist (inbox).
 */
@ApiController
public class CollateralWorkFlowController {

    private final ICollateralWorkflowService workflowService;
    private final ICollateralAuthorizationService authorizationService;

    public CollateralWorkFlowController(ICollateralWorkflowService workflowService,
                                        ICollateralAuthorizationService authorizationService) {
        this.workflowService = workflowService;
        this.authorizationService = authorizationService;
    }

    /** Send for Approval: PUT /api/collaterals/workflow/startprocess { "businessProcessId": checklist id } */
    @PutMapping("/collaterals/workflow/startprocess")
    public ResponseEntity<CollateralChecklistDto> startProcess(@RequestBody CollateralWorkflowRequest request,
                                                               HttpServletRequest httpServletRequest) {
        String user = currentUser(httpServletRequest);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.ok(workflowService.startWorkflowProcessInstance(request.getBusinessProcessId(), user));
    }

    /** Approve: PUT /api/collaterals/workflow/approvetask { businessProcessId, processInstanceId (or task id) } */
    @PutMapping("/collaterals/workflow/approvetask")
    public ResponseEntity<CollateralChecklistDto> approveTask(@RequestBody CollateralWorkflowRequest request,
                                                              HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(workflowService.approveTask(request.getProcessInstanceId(),
                request.getBusinessProcessId(), currentUser(httpServletRequest)));
    }

    /** Reject: PUT /api/collaterals/workflow/rejecttask { businessProcessId, processInstanceId, rejectionReason } */
    @PutMapping("/collaterals/workflow/rejecttask")
    public ResponseEntity<CollateralChecklistDto> rejectTask(@RequestBody CollateralWorkflowRequest request,
                                                             HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(workflowService.rejectTask(request.getProcessInstanceId(),
                request.getBusinessProcessId(), request.getRejectionReason(), currentUser(httpServletRequest)));
    }

    private static String currentUser(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
        return request.getUserPrincipal().getName();
    }
}
