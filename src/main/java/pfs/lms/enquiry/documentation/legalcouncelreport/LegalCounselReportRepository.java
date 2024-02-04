package pfs.lms.enquiry.documentation.legalcouncelreport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LegalCounselReportRepository extends JpaRepository<LegalCounselReport, UUID> {

    List<LegalCounselReport> findByLegalCounselId(UUID legalCounselId);

    List<LegalCounselReport> findByLegalCounselIdAndDeleteFlag(UUID legalCounselId, Boolean deleteFlag);
}
