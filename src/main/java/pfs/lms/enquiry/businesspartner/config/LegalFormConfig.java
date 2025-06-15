package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
import pfs.lms.enquiry.businesspartner.domain.LegalForm;
 import pfs.lms.enquiry.businesspartner.repository.LegalFormRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class LegalFormConfig implements CommandLineRunner {

    private final LegalFormRepository legalFormRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        
        
        LegalForm legalForm = new LegalForm();


        legalForm = legalFormRepository.findByCode("");if (legalForm == null){ legalForm = new LegalForm( "",""); legalFormRepository.save(legalForm); }

        legalForm = legalFormRepository.findByCode("01");if (legalForm == null){ legalForm = new LegalForm( "01","Proprietorship"); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("02");if (legalForm == null){ legalForm = new LegalForm( "02","Pvt. Ltd."    ); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("03");if (legalForm == null){ legalForm = new LegalForm( "03","Partnership Firm"     ); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("04");if (legalForm == null){ legalForm = new LegalForm( "04","Co. Op"); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("05");if (legalForm == null){ legalForm = new LegalForm( "05","Pub. Ltd. Co"    ); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("06");if (legalForm == null){ legalForm = new LegalForm( "06","Joint Hindu Family"     ); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("07");if (legalForm == null){ legalForm = new LegalForm( "07","LLP"     ); legalFormRepository.save(legalForm); }
        legalForm = legalFormRepository.findByCode("08");if (legalForm == null){ legalForm = new LegalForm( "08","Others"     ); legalFormRepository.save(legalForm); }

        

        return;
    }
}