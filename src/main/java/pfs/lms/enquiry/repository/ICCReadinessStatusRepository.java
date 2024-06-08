package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.ICCReadinessStatus;

import java.util.UUID;

public interface ICCReadinessStatusRepository extends JpaRepository<ICCReadinessStatus, UUID> {

    ICCReadinessStatus findByCode(String code);

    ICCReadinessStatus findByValue(String value);
}
