package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntityFieldStatus;
import pfs.lms.enquiry.businesspartner.domain.BupaRoleEntitySetFieldStatus;

import java.util.List;

public interface BupaRoleEntitySetFieldStatusRepository extends JpaRepository<BupaRoleEntitySetFieldStatus, String> {

   List<BupaRoleEntitySetFieldStatus> findByBupaRoleCode(String code);
   BupaRoleEntitySetFieldStatus findByBupaRoleCodeAndEntitySetAndFieldNameAndKeyFieldValue( String role, String entity, String fieldName, String keyFieldValue);


}
