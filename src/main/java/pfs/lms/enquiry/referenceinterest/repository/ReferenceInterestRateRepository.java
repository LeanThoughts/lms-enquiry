package pfs.lms.enquiry.referenceinterest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;


    public   interface ReferenceInterestRateRepository extends JpaRepository<ReferenceInterestRate, Integer> {
        public ReferenceInterestRate findByCode(String code);
    }

