package pfs.lms.enquiry.applicationfee.termsheet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TermSheetRepository extends JpaRepository<TermSheet, UUID> {

    List<TermSheet> findByApplicationFeeIdOrderBySerialNumber(UUID applicationFeeId);
}
