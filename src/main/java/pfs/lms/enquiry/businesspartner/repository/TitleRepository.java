package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;
import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Long> {
@Override
    @PreAuthorize(READ)
    Optional<Title> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<Title> findAll();

    @Override
    @PreAuthorize(READ)
    Page<Title> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends Title> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(Title entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);


    @PreAuthorize(READ)
    Title findByCode(String code);
    @PreAuthorize(READ)
    List<Title> findByPartnerCategory(String code);

}
