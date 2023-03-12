package pfs.lms.enquiry.appraisal.projectlocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubLocationDetailRepository extends JpaRepository<SubLocationDetail, UUID> {

    List<SubLocationDetail> findByLoanAppraisalId(UUID loanAppraisalId);
}
