package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.domain.PartnerRoleType;

import java.util.List;
import java.util.UUID;

public interface PartnerRepository extends JpaRepository<Partner, UUID> {
    List<Partner> findByEmail(String email);
    Partner findByUserName(String username);
    Partner findByPartyNumber(Integer partyNumber);

    List<Partner> findByPartyNumberBetween(Integer fromPartnerNumber, Integer toPartnerNumber);
    List<Partner> findByPartyName1Contains(String name);
    List<Partner> findByEmailContains(String email);

    List<Partner> findByPartyName1StartingWith(String alphabet);

    List<Partner> findByPartyRole(String partyRole);
    List <Partner> findByPartnerRoleTypes(PartnerRoleType partnerRoleType);

    @Query("select e from Partner e where trim(concat(trim(COALESCE(partyName1, '')), ' ', trim(COALESCE(partyName2, '')))) " +
            "like %:searchString%")
    List<Partner> findBySearchString(@Param("searchString") String searchString);
}
