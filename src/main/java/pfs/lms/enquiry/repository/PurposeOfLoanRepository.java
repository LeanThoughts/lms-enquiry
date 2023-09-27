package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.PurposeOfLoan;

import java.util.UUID;

public interface PurposeOfLoanRepository extends JpaRepository<PurposeOfLoan, UUID> {

    PurposeOfLoan getByCode(String code);
}
