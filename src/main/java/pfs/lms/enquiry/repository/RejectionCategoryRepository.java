package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.RejectionCategory;

import java.util.UUID;

public interface RejectionCategoryRepository extends JpaRepository<RejectionCategory, UUID> {
}
