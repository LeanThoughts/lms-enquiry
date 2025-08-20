package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.WorkflowApprover;

import java.util.UUID;

/**
 * Created by sajeev on 09-Mar-21.
 */
public interface WorkflowApproverRepository extends JpaRepository<WorkflowApprover, Long> {

    public WorkflowApprover findByProcessName(String processName);
    public WorkflowApprover findByDepartmentCodeAndProcessName(String departmentCode, String processName );
}
