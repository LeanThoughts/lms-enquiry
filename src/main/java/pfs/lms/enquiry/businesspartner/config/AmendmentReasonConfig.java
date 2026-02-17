package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.AmendmentReason;
import pfs.lms.enquiry.businesspartner.repository.AmendmentReasonRepository;

@Slf4j
@Component
@RequiredArgsConstructor

public class AmendmentReasonConfig implements CommandLineRunner {

    private final AmendmentReasonRepository amendmentReasonRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        AmendmentReason amendmentReason = new AmendmentReason();


        amendmentReason = amendmentReasonRepository.findByCode("1");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "1","Change in Sanction Amt"); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("2");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "2","Timeline Extension"    ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("3");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "3","Waiver of Condition"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("4");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "4","Language Modification"); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("5");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "5","Change in Intt Rate"    ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("6");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "6","Reimbursement"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("7");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "7","Waiver of billed amt"     ); amendmentReasonRepository.save(amendmentReason); }

        amendmentReason = amendmentReasonRepository.findByCode("8");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "8","Downselling"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("9");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "9","Change in Capacity"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("10");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "10","Oversubscription of Facility"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("11");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "11","Reduction in Project Cost"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("12");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "12","Bridge Debt not converted in Term Debt"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("13");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "13","Tariff reduction & Viability"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("14");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "14","Group Exposure"     ); amendmentReasonRepository.save(amendmentReason); }
        amendmentReason = amendmentReasonRepository.findByCode("15");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "15","Change in Sanction Terms-Other"     ); amendmentReasonRepository.save(amendmentReason); }

        return;
    }
}