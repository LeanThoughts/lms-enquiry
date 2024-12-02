package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.CountryCode;
import pfs.lms.enquiry.businesspartner.domain.SanctionAuthority;
import pfs.lms.enquiry.businesspartner.domain.Title;
import pfs.lms.enquiry.businesspartner.repository.SanctionAuthorityRepository;
import pfs.lms.enquiry.businesspartner.repository.TitleRepository;
import pfs.lms.enquiry.sanction.Sanction;

@Slf4j
@Component
@RequiredArgsConstructor
public class SanctionAuthorityConfig implements CommandLineRunner {

    private final SanctionAuthorityRepository sanctionAuthorityRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        SanctionAuthority sanctionAuthority = new SanctionAuthority();


        sanctionAuthority = sanctionAuthorityRepository.findByCode("01");if (sanctionAuthority == null){ sanctionAuthority = new SanctionAuthority( "01","MD&CEO"); sanctionAuthorityRepository.save(sanctionAuthority); }
        sanctionAuthority = sanctionAuthorityRepository.findByCode("02");if (sanctionAuthority == null){ sanctionAuthority = new SanctionAuthority( "02","Board Committee"    ); sanctionAuthorityRepository.save(sanctionAuthority); }
        sanctionAuthority = sanctionAuthorityRepository.findByCode("03");if (sanctionAuthority == null){ sanctionAuthority = new SanctionAuthority( "03","Board"     ); sanctionAuthorityRepository.save(sanctionAuthority); }

        return;
    }
}