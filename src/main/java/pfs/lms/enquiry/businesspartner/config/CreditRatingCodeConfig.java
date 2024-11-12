package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingAgency;
import pfs.lms.enquiry.businesspartner.domain.CreditRatingCode;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingAgencyRepository;
import pfs.lms.enquiry.businesspartner.repository.CreditRatingCodeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditRatingCodeConfig implements CommandLineRunner {

    private final CreditRatingCodeRepository creditRatingCodeRepository;
 
    @Override
    public void run(String... strings) throws Exception {
        CreditRatingCode creditRatingCode = new CreditRatingCode();

        creditRatingCode = creditRatingCodeRepository.findByCode("44");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","BWR A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("1");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","BWR A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("2");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","BWR A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("3");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","BWR A- (CE)"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("45");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","BWR BB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("4");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("5");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("6");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE A-/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("7");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("8");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE B+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("9");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("10");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("11");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("12");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BBB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("13");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("14");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BBB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("15");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE BBB+/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("16");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CARE D"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("17");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("18");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("19");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL A/Negative"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("20");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL AA-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("21");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL B"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("22");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL BB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("23");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL BB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("24");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL BBB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("25");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("26");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","CRISIL BBB-/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("27");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("28");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("29");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA A-/Positive"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("30");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("31");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA BBB"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("32");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("33");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA BBB+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("34");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","ICRA D"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("35");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND A"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("36");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND A-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("37");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND A-/Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("38");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND A+"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("39");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND AA- /Stable"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("40");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND AA-/Positive"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("41");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND C"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("42");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND D"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("46");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","IND BBB (India Ratings BBB)"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("43");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","Informics BBB-"); creditRatingCodeRepository.save(creditRatingCode); }
        creditRatingCode = creditRatingCodeRepository.findByCode("99");if (creditRatingCode == null){ creditRatingCode = new CreditRatingCode(null,"CRISIL","Not Available"); creditRatingCodeRepository.save(creditRatingCode); }

        return;
    }
}