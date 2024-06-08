package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.ProposalType;

import java.util.UUID;

public interface ProposalTypeRepository extends JpaRepository<ProposalType, UUID> {

    ProposalType findByCode(String code);

    ProposalType findByValue(String value);
}
