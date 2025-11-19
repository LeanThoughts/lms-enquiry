package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;

import java.util.List;

public interface BupaRoleEntityFieldStatusRepository extends JpaRepository<BupaRoleEntityFieldStatus, String> {

   List<BupaRoleEntityFieldStatus> findByBupaRoleCode(String code);
   BupaRoleEntityFieldStatus findByBupaRoleCodeAndEntityAndAndFieldName( String role, String entity, String fieldName);

}
