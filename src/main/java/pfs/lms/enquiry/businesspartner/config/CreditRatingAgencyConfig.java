package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingAgencyRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditRatingAgencyConfig implements CommandLineRunner {

    private final CreditRatingAgencyRepository creditRatingAgencyRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        CreditRatingAgency creditRatingAgency = new CreditRatingAgency();


        creditRatingAgency = creditRatingAgencyRepository.findByCode("CRISIL");if (creditRatingAgency == null){ creditRatingAgency = new CreditRatingAgency("CRISIL","CRISIL"); creditRatingAgencyRepository.save(creditRatingAgency); }
        creditRatingAgency = creditRatingAgencyRepository.findByCode("ICRA");if (creditRatingAgency == null){ creditRatingAgency = new CreditRatingAgency("ICRA","ICRA"); creditRatingAgencyRepository.save(creditRatingAgency); }
        creditRatingAgency = creditRatingAgencyRepository.findByCode("BWR");if (creditRatingAgency == null){ creditRatingAgency = new CreditRatingAgency("BWR","BWR"); creditRatingAgencyRepository.save(creditRatingAgency); }
        creditRatingAgency = creditRatingAgencyRepository.findByCode("IND");if (creditRatingAgency == null){ creditRatingAgency = new CreditRatingAgency("IND","IND"); creditRatingAgencyRepository.save(creditRatingAgency); }
        creditRatingAgency = creditRatingAgencyRepository.findByCode("CARE");if (creditRatingAgency == null){ creditRatingAgency = new CreditRatingAgency("CARE","CARE"); creditRatingAgencyRepository.save(creditRatingAgency); }
        creditRatingAgency = creditRatingAgencyRepository.findByCode("Informics");if (creditRatingAgency == null){ creditRatingAgency = new CreditRatingAgency("Informics","Informics"); creditRatingAgencyRepository.save(creditRatingAgency); }

        return;
    }
}