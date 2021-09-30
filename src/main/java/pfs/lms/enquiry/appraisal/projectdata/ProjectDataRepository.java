package pfs.lms.enquiry.appraisal.projectdata;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectDataRepository extends JpaRepository<ProjectData, UUID> {

    ProjectData findByLoanAppraisalId(UUID loanAppraisalId);
}
