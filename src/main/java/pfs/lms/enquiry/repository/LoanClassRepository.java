package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.LoanClass;

import java.util.UUID;

public interface LoanClassRepository extends JpaRepository<LoanClass, UUID> {
    LoanClass findByCode(String code);
}
