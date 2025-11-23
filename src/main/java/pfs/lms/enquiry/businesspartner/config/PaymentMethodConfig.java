package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.PaymentTerms;
import pfs.lms.enquiry.businesspartner.repository.PaymentTermsRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMethodConfig implements CommandLineRunner {

    private final PaymentTermsRepository paymentTermsRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        PaymentTerms paymentTerms = new PaymentTerms();
        paymentTerms = paymentTermsRepository.findById("A");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Cheque-Incomming(Received)"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("B");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Demand Draft-Incomming"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("N");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Bank Transfer-Incomming"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("C");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Cheque  payment(Others) "); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("D");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Demand Draft-Outgoing "); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("E");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null," "); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("H");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Cheque Payment HDFC Bank"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("I");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"ICICI Bank Payment"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("P");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Cheque Payment PNB Bank"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("S");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"SBI Bank Payment"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("T");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Bank Transfer-Outgoing"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("Y");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"Yes Bank Payment"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("  ");if (paymentTerms == null){ paymentTerms = new PaymentTerms(null,"  "); paymentTermsRepository.save(paymentTerms); }

        return;
    }
}