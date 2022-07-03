package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.FinancingType;

import java.util.UUID;

public interface FinancingTypeRepository extends JpaRepository<FinancingType, UUID> {

    FinancingType findByCode(String code);
}
