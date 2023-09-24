package pfs.lms.enquiry.sanction.reasonfordelay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SanctionReasonForDelayRepository extends JpaRepository<SanctionReasonForDelay, UUID> {

    List<SanctionReasonForDelay> findBySanctionId(UUID sanctionId);
}
