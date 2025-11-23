package pfs.lms.enquiry.businesspartner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.businesspartner.domain.SortKey;
import pfs.lms.enquiry.businesspartner.domain.Title;

import java.util.List;

public interface SortKeyRepository extends JpaRepository<SortKey, Long> {

    SortKey findById(String id);

}
