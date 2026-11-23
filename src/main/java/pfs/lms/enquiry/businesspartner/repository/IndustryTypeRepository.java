package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.domain.IndustryType;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface IndustryTypeRepository extends JpaRepository<IndustryType, Long> {
@Override
    @PreAuthorize(READ)
    Optional<IndustryType> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<IndustryType> findAll();

    @Override
    @PreAuthorize(READ)
    Page<IndustryType> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends IndustryType> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(IndustryType entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    IndustryType findIndustryTypeByCode(String code);
    @PreAuthorize(READ)
    IndustryType findIndustryTypeByCodeAndIndustrySystem(String code, IndustrySystem industrySystem);
    @PreAuthorize(READ)
    List<IndustryType> findByIndustrySystemId(Long industrySystemId);
    @PreAuthorize(READ)
    IndustryType findIndustryTypeById(Long industryTypeId);
}
