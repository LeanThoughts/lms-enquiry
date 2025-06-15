package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.repository.AmendmentReasonRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateForConfig implements CommandLineRunner {

    private final AmendmentReasonRepository amendmentReasonRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        //AmendmentReason amendmentReason = new AmendmentReason();


        //samendmentReason = amendmentReasonRepository.findByCode("1");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "1","Change in Sanction Amt-Others"); amendmentReasonRepository.save(amendmentReason); }


        return;
    }
}