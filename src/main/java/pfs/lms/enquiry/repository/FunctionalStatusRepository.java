package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.FunctionalStatus;

import java.util.UUID;

public interface FunctionalStatusRepository extends JpaRepository<FunctionalStatus, UUID> {

    FunctionalStatus findByCode(Integer code);

    FunctionalStatus findByValue(String value);
}
