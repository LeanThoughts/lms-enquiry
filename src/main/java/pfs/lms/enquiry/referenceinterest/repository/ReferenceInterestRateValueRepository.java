package pfs.lms.enquiry.referenceinterest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReferenceInterestRateValueRepository extends JpaRepository<ReferenceInterestRateValue, UUID> {
    List<ReferenceInterestRateValue> findByReferenceInterestRateId(Long referenceInterestRateId);
    ReferenceInterestRateValue findByReferenceInterestRateIdAndValidFromDate(Long id, LocalDate validFromDate);
}
