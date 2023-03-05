package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.FinancingType;
import pfs.lms.enquiry.domain.TRAAccountType;

import java.util.UUID;

public interface TRAAccountTypeRepository extends JpaRepository<TRAAccountType, UUID> {

    TRAAccountType findByCode(String code);
}
