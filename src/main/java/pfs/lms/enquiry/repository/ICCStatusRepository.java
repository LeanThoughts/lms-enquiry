package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.ICCStatus;

import java.util.UUID;

public interface ICCStatusRepository extends JpaRepository<ICCStatus, UUID> {

    ICCStatus findByCode(String code);

    ICCStatus findByValue(String value);
}
