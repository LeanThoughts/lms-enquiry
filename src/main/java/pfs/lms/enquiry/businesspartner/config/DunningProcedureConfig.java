package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.DunningProcedure;
import pfs.lms.enquiry.businesspartner.domain.PaymentMethod;
import pfs.lms.enquiry.businesspartner.repository.DunningProcedureRepository;
import pfs.lms.enquiry.businesspartner.repository.PaymentMethodRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DunningProcedureConfig implements CommandLineRunner {

    private final DunningProcedureRepository dunningProcedureRepository;

    @Override
    public void run(String... strings) throws Exception {
        DunningProcedure dunningProcedure = new DunningProcedure();
        dunningProcedure = dunningProcedureRepository.findById("PFS");if (dunningProcedure == null){ dunningProcedure = new DunningProcedure("PFS","PFS"); dunningProcedureRepository.save(dunningProcedure); }

        return;
    }
}