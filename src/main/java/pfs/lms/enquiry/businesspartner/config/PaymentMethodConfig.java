package pfs.lms.enquiry.businesspartner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.domain.PaymentMethod;
import pfs.lms.enquiry.businesspartner.repository.PaymentMethodRepository;
import pfs.lms.enquiry.businesspartner.repository.PaymentTermsRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMethodConfig implements CommandLineRunner {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void run(String... strings) throws Exception {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod = paymentMethodRepository.findById("A");if (paymentMethod == null){ paymentMethod = new PaymentMethod("A","Cheque-Incomming(Received)"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("B");if (paymentMethod == null){ paymentMethod = new PaymentMethod("B","Demand Draft-Incomming"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("N");if (paymentMethod == null){ paymentMethod = new PaymentMethod("N","Bank Transfer-Incomming"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("C");if (paymentMethod == null){ paymentMethod = new PaymentMethod("C","Cheque  payment(Others) "); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("D");if (paymentMethod == null){ paymentMethod = new PaymentMethod("D","Demand Draft-Outgoing "); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("E");if (paymentMethod == null){ paymentMethod = new PaymentMethod("E"," "); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("H");if (paymentMethod == null){ paymentMethod = new PaymentMethod("H","Cheque Payment HDFC Bank"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("I");if (paymentMethod == null){ paymentMethod = new PaymentMethod("I","ICICI Bank Payment"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("P");if (paymentMethod == null){ paymentMethod = new PaymentMethod("P","Cheque Payment PNB Bank"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("S");if (paymentMethod == null){ paymentMethod = new PaymentMethod("S","SBI Bank Payment"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("T");if (paymentMethod == null){ paymentMethod = new PaymentMethod("T","Bank Transfer-Outgoing"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("Y");if (paymentMethod == null){ paymentMethod = new PaymentMethod("Y","Yes Bank Payment"); paymentMethodRepository.save(paymentMethod); }
        paymentMethod = paymentMethodRepository.findById("  ");if (paymentMethod == null){ paymentMethod = new PaymentMethod("","  "); paymentMethodRepository.save(paymentMethod); }

        return;
    }
}