package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.PresentedInICC;

import java.util.UUID;

public interface PresentedInICCRepository extends JpaRepository<PresentedInICC, UUID> {

    PresentedInICC findByCode(String code);

    PresentedInICC findByValue(String value);
}
