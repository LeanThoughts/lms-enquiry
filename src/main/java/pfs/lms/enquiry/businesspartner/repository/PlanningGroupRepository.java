package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.PlanningGroup;
import pfs.lms.enquiry.businesspartner.domain.SortKey;

public interface PlanningGroupRepository extends JpaRepository<PlanningGroup, Long> {

    PlanningGroup findById(String id);

}
