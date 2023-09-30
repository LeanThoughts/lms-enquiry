package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.FeeType;
import pfs.lms.enquiry.domain.SanctionType;

import java.util.UUID;

public interface SanctionTypeRepository extends JpaRepository<SanctionType, UUID> {

    SanctionType getSanctionTypeByCode(String code);
}
