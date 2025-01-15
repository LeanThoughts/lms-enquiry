package pfs.lms.enquiry.referenceinterest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRateValue;

import java.util.UUID;


public   interface ReferenceInterestRateValueRepository extends JpaRepository<ReferenceInterestRateValue, UUID> {
 }

