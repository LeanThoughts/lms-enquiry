package pfs.lms.enquiry.monitoring.valuer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValuerReportAndFeeRepository extends JpaRepository<ValuerReportAndFee, String> {

    List<ValuerReportAndFee> findByValuer(Valuer valuer);
}
