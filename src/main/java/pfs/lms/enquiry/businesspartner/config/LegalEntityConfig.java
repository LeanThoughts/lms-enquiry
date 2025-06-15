package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.LegalEntity;
import pfs.lms.enquiry.businesspartner.repository.LegalEntityRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class LegalEntityConfig implements CommandLineRunner {

    private final LegalEntityRepository legalEntityRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        LegalEntity legalEntity = new LegalEntity();

        legalEntity = legalEntityRepository.findByCode("");if (legalEntity == null){ legalEntity = new LegalEntity( "",""); legalEntityRepository.save(legalEntity); }

        legalEntity = legalEntityRepository.findByCode("1");if (legalEntity == null){ legalEntity = new LegalEntity( "01","Proprietorship"); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("2");if (legalEntity == null){ legalEntity = new LegalEntity( "02","Pvt. Ltd."    ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("3");if (legalEntity == null){ legalEntity = new LegalEntity( "03","Partnership Firm"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("4");if (legalEntity == null){ legalEntity = new LegalEntity( "04","Co. Op"); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("5");if (legalEntity == null){ legalEntity = new LegalEntity( "05","Pub. Ltd. Co"    ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("6");if (legalEntity == null){ legalEntity = new LegalEntity( "06","Joint Hindu Family"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("7");if (legalEntity == null){ legalEntity = new LegalEntity( "07","LLP"     ); legalEntityRepository.save(legalEntity); }
        legalEntity = legalEntityRepository.findByCode("8");if (legalEntity == null){ legalEntity = new LegalEntity( "08","Others"     ); legalEntityRepository.save(legalEntity); }


        return;
    }
}