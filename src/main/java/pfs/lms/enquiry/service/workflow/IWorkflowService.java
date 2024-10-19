package pfs.lms.enquiry.service.workflow;

import pfs.lms.enquiry.dto.WorkflowTaskDTO;
import pfs.lms.enquiry.exception.HandledException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * Created by sajeev on 09-Mar-21.
 */
public interface IWorkflowService {

    public Object startWorkflowProcessInstance (UUID businessProcessId,
                                                     String requestorName,
                                                     String requestorEmail,
                                                     String processName) throws HandledException, CloneNotSupportedException;


    public Object approveTask(String processInstanceId, UUID businessProcessId, String processName, String userName) throws Exception;

    public Object rejectTask(String processInstanceId, UUID businessProcessId, String processName, String rejectionReason,String requestorEmail) throws CloneNotSupportedException;


    // Workflow Tasks for an User
    public  List<WorkflowTaskDTO>  getTasks(HttpServletRequest httpServletRequest);

}
