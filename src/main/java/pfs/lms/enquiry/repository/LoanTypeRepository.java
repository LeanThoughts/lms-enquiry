package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.LoanType;

import java.util.UUID;

public interface LoanTypeRepository extends JpaRepository<LoanType, UUID> {

    LoanType getLoanTypeByCode(String code);
    LoanType getLoanTypeByValue(String code);
}
