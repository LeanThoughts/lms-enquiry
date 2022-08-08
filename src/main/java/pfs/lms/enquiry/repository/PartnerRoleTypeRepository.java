package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.PartnerRoleType;

import java.util.List;
import java.util.UUID;

public interface PartnerRoleTypeRepository extends JpaRepository<PartnerRoleType, UUID> {

    //PartnerRoleType findByRoleCode(String roleCode);

    List<PartnerRoleType> findByRoleCode(String roleCode);

}
