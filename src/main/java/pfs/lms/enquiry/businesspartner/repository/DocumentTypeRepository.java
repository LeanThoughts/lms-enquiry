package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pfs.lms.enquiry.businesspartner.domain.DocumentType;

import java.util.List;
import java.util.Optional;

import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.READ;
import static pfs.lms.enquiry.businesspartner.BusinessPartnerRepositorySecurity.WRITE;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    @Override
    @PreAuthorize(READ)
    Optional<DocumentType> findById(Long id);

    @Override
    @PreAuthorize(READ)
    List<DocumentType> findAll();

    @Override
    @PreAuthorize(READ)
    Page<DocumentType> findAll(Pageable pageable);

    @Override
    @PreAuthorize(WRITE)
    <S extends DocumentType> S save(S entity);

    @Override
    @PreAuthorize(WRITE)
    void delete(DocumentType entity);

    @Override
    @PreAuthorize(WRITE)
    void deleteById(Long id);

    @PreAuthorize(READ)
    DocumentType findByCode(String code);
}