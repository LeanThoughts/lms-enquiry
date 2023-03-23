package pfs.lms.enquiry.monitoring.npa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NPADetailRepository extends JpaRepository<NPADetail, UUID> {

    List<NPADetail> findByNpaOOrderByLineItemNumberDesc(NPA npa);
}
