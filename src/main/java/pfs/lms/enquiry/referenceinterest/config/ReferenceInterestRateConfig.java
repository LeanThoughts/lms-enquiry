package pfs.lms.enquiry.referenceinterest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReferenceInterestRateConfig implements CommandLineRunner {

    @Autowired
    private ReferenceInterestRateRepository referenceInterestRateRepository;

    @Override
    public void run(String... args) throws Exception {


        ReferenceInterestRate referenceInterestRate = referenceInterestRateRepository.findByCode("LMS_PFS_BR");
        if (referenceInterestRate == null) {
            referenceInterestRate = new ReferenceInterestRate(null, "LMS_PFS_BR", "PFS Base Rate");
            referenceInterestRateRepository.save(referenceInterestRate);
        }

    }
}
