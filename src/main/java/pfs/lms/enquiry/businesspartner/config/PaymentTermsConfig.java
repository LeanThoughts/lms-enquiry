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

        paymentTerms = paymentTermsRepository.findById("");if (paymentTerms == null){ paymentTerms = new PaymentTerms(" "," "); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0001");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0001","Allocationnumber"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0002");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0002","Postingdate"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0003");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0003","Documentdate"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0004");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0004","Doc.currencyamount"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0005");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0005","Costcenter"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0006");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0006","Purchaseorderno."); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0007");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0007","Purchaseorder"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0008");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0008","One-timename/"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0009");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0008","Costcenter"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("0010");if (paymentTerms == null){ paymentTerms = new PaymentTerms("0010","Currencykey"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT00");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT00","Projectnumber"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT15");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT15","Fiscalyear,month"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT30");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT30","Test0"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT45");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT45","Test1"); paymentTermsRepository.save(paymentTerms); }
        paymentTerms = paymentTermsRepository.findById("NT60");if (paymentTerms == null){ paymentTerms = new PaymentTerms("NT60","Test5"); paymentTermsRepository.save(paymentTerms); }

        return;
    }
}