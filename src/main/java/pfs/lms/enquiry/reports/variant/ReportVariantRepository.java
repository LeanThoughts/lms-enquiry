package pfs.lms.enquiry.reports.variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Not exported by Spring Data REST: access goes through {@link ReportVariantController} only. */
@RepositoryRestResource(exported = false)
public interface ReportVariantRepository extends JpaRepository<ReportVariant, UUID> {

    /** The variants a user sees: the own ones and the shared ones. */
    @Query("select v from ReportVariant v where v.report = :report and (v.ownerName = :user or v.shared = true) order by v.name, v.ownerName")
    List<ReportVariant> findVisible(@Param("report") String report, @Param("user") String user);

    Optional<ReportVariant> findByReportAndOwnerNameAndName(String report, String ownerName, String name);

    List<ReportVariant> findByReportAndOwnerNameAndDefaultVariantTrue(String report, String ownerName);
}
