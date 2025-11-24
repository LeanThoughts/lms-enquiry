package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleCustomerFieldValues;

public interface BupaRoleCustomerFieldValuesRepository extends JpaRepository<BupaRoleCustomerFieldValues, String> {


   BupaRoleCustomerFieldValues findByBupaRoleCode(String code);
}
