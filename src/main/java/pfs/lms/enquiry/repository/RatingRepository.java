package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.Rating;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
}
