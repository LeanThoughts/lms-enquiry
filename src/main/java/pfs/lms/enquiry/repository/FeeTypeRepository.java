package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.AssistanceType;
import pfs.lms.enquiry.domain.FeeType;

import java.util.UUID;

public interface FeeTypeRepository extends JpaRepository<FeeType, UUID> {

    FeeType getFeeTypeByCode(String code);
}
