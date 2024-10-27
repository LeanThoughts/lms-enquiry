package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.IdentificationCategory;
import pfs.lms.enquiry.businesspartner.domain.IndustrySystem;
import pfs.lms.enquiry.businesspartner.repository.IdentificationCategoryRepository;
import pfs.lms.enquiry.businesspartner.repository.IndustrySystemRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndustrySystemConfig implements CommandLineRunner {

    private final IndustrySystemRepository industrySystemRepository;

    @Override
    public void run(String... strings) throws Exception {

        IndustrySystem industrySystem = new IndustrySystem();
        industrySystem = industrySystemRepository.findIndustrySystemByCode("0001");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "0001", "Standard Industry System");
            industrySystemRepository.save(industrySystem);
        }
        industrySystem = industrySystemRepository.findIndustrySystemByCode("10");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "10", "Public-Central Govt");
            industrySystemRepository.save(industrySystem);
        }
        industrySystem = industrySystemRepository.findIndustrySystemByCode("11");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "11", "Public-State Govt");
            industrySystemRepository.save(industrySystem);
        }
        industrySystem = industrySystemRepository.findIndustrySystemByCode("12");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "12", "Public-Others");
            industrySystemRepository.save(industrySystem);
        }
        industrySystem = industrySystemRepository.findIndustrySystemByCode("20");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "20", "Co-Operative");
            industrySystemRepository.save(industrySystem);
        }
        industrySystem = industrySystemRepository.findIndustrySystemByCode("30");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "30", "Private");
            industrySystemRepository.save(industrySystem);
        }
        industrySystem = industrySystemRepository.findIndustrySystemByCode("40");
        if (industrySystem == null) {
            industrySystem = new IndustrySystem(null, "40", "Joint");
            industrySystemRepository.save(industrySystem);
        }

        return;
    }
}