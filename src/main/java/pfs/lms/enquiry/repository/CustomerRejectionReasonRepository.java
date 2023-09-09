package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.CustomerRejectionReason;
import pfs.lms.enquiry.domain.FinancingType;

import java.util.UUID;

public interface CustomerRejectionReasonRepository extends JpaRepository<CustomerRejectionReason, UUID> {

    CustomerRejectionReason findByCode(String code);
}
