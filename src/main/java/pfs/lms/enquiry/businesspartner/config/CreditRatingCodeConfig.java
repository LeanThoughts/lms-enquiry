package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingCodeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditRatingCodeConfig implements CommandLineRunner {

    private final CreditRatingCodeRepository creditRatingCodeRepository;
 
    @Override
    public void run(String... strings) throws Exception {
        CreditRatingCode creditRatingCode = new CreditRatingCode();
        creditRatingCode = creditRatingCodeRepository.findByCode("BWR A+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("BWR A+","BWR A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("BWR A");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("BWR A","BWR A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("BWR A-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("BWR A-","BWR A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("BWR A- (CE)");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("BWR A- (CE)","BWR A- (CE)"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("BWR BB");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("BWR BB","BWR BB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE A");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE A","CARE A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE A-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE A-","CARE A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE A-/Stable");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE A-/Stable","CARE A-/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE A+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE A+","CARE A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE B+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE B+","CARE B+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BB");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BB","CARE BB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BB-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BB-","CARE BB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BB+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BB+","CARE BB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BBB");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BBB","CARE BBB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BBB-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BBB-","CARE BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BBB+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BBB+","CARE BBB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE BBB+/Stable");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE BBB+/Stable","CARE BBB+/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CARE D");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CARE D","CARE D"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL A");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL A","CRISIL A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL A-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL A-","CRISIL A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL A/Negative");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL A/Negative","CRISIL A/Negative"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL AA-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL AA-","CRISIL AA-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL B");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL B","CRISIL B"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL BB");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL BB","CRISIL BB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL BB+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL BB+","CRISIL BB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL BBB");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL BBB","CRISIL BBB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL BBB-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL BBB-","CRISIL BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("CRISIL BBB-/Stable");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("CRISIL BBB-/Stable","CRISIL BBB-/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA A");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA A","ICRA A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA A-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA A-","ICRA A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA A-/Positive");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA A-/Positive","ICRA A-/Positive"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA A+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA A+","ICRA A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA BBB");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA BBB","ICRA BBB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA BBB-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA BBB-","ICRA BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA BBB+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA BBB+","ICRA BBB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("ICRA D");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("ICRA D","ICRA D"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND A");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND A","IND A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND A-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND A-","IND A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND A-/Stable");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND A-/Stable","IND A-/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND A+");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND A+","IND A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND AA- /Stable");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND AA- /Stable","IND AA- /Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND AA-/Positive");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND AA-/Positive","IND AA-/Positive"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND C");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND C","IND C"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND D");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND D","IND D"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("IND BBB (India Ratings BBB)");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("IND BBB (India Ratings BBB)","IND BBB (India Ratings BBB)"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("Informics BBB-");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("Informics BBB-","Informics BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("Not Available");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode("Not Available","Not Available"); creditRatingCodeRepository.save(creditRatingCode); }

        return;
    }
}