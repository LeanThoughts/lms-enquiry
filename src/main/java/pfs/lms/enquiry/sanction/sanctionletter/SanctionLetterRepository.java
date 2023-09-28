package pfs.lms.enquiry.sanction.sanctionletter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, UUID> {

    List<SanctionLetter> findBySanctionId(UUID sanctionId);
}
