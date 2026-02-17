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
public class PaymentTermsConfig implements CommandLineRunner {

    private final PaymentTermsRepository paymentTermsRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        PaymentTerms paymentTerms = new PaymentTerms();

        paymentTerms = paymentTermsRepository.findById("0001");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0001","Payable immediately Due net, "); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0002");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0002","within 14 days 2 % cash discount, within 30 days Due net"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0003");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0003","within 14 days 3 % cash discount, within 14 days 3 % cash discount"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0004");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0004",", within 20 days 2 % cash discount,within 30 days Due net,within 30 days Due net"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0005");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0005","Payable immediately Due net, Baseline date on End of the month"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0006");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0006","Payable immediately Due net, Baseline date on 10 of next month"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0007");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0007","Before End of the month 4 % cash discount, Before 15 of the next month ;; 2 % cash discountBefore End of the next month ;; Due net"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0008");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0008","For incoming invoices until 15 of the month, within 14 days 2 % cash discount,within 30 days 1.5 % cash discount"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0009");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0009","Payable in 3 partial amounts"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0010");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0010","Payable immediately Due net"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT00");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT00","Payable upon receipt"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT15");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT15","Net due in 15 days"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT30");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT30","Net due in 30 days"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT45");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT45","Net due in 45 days"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT60");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT60","Net due in 60 days"); paymentTermsRepository.save(paymentTerms); }

        return;
    }
}