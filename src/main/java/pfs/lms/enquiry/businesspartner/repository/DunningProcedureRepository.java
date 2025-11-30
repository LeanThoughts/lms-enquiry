package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.DunningProcedure;
import pfs.lms.enquiry.businesspartner.domain.SortKey;

public interface DunningProcedureRepository extends JpaRepository<DunningProcedure, Long> {

    DunningProcedure findById(String id);

}
