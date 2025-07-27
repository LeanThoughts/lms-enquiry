package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import pfs.lms.enquiry.businesspartner.repository.LegalEntityRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class LegalEntityConfig implements CommandLineRunner {

    private final LegalEntityRepository legalEntityRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        LegalEntity legalEntity = new LegalEntity();

        //legalEntity = legalEntityRepository.findByCode(" ");if (legalEntity == null){ legalEntity = new LegalEntity( " ",""); legalEntityRepository.save(legalEntity); }

        legalEntity = legalEntityRepository.findByCode("01");if (legalEntity == null){ legalEntity = new LegalEntity( "01","Public Sec Bank"); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("02");if (legalEntity == null){ legalEntity = new LegalEntity( "02","Pvt Sec Bank"    ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("03");if (legalEntity == null){ legalEntity = new LegalEntity( "03","Insurance Comp"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("04");if (legalEntity == null){ legalEntity = new LegalEntity( "04","Industry"); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("05");if (legalEntity == null){ legalEntity = new LegalEntity( "05","Other Body"    ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("06");if (legalEntity == null){ legalEntity = new LegalEntity( "06","Municipal Body"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("07");if (legalEntity == null){ legalEntity = new LegalEntity( "07","Mutual Fund"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("08");if (legalEntity == null){ legalEntity = new LegalEntity( "08","NBFC"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("09");if (legalEntity == null){ legalEntity = new LegalEntity( "09","Fin Instn"    ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("10");if (legalEntity == null){ legalEntity = new LegalEntity( "10","Trading Comp"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("11");if (legalEntity == null){ legalEntity = new LegalEntity( "11","Others"     ); legalEntityRepository.save(legalEntity); }


        return;
    }
}