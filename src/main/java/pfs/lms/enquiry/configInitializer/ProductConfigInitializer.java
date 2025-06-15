package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.businesspartner.repository.AmendmentReasonRepository;
import pfs.lms.enquiry.domain.Product;
import pfs.lms.enquiry.repository.ProductRepository;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConfigInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
 
    @Override
    public void run(String... strings) throws Exception {

        Product product = new Product();


        //samendmentReason = amendmentReasonRepository.findByCode("1");if (amendmentReason == null){ amendmentReason = new AmendmentReason( "1","Change in Sanction Amt-Others"); amendmentReasonRepository.save(amendmentReason); }

            product = productRepository.findByCode("301"); if (product == null) { Product pr1 = new Product("301", "Bridge Loan"); productRepository.save(product); };
            product = productRepository.findByCode("302"); if (product == null) { Product pr1 = new Product("302", "Short Term Loan"); productRepository.save(product); };
            product = productRepository.findByCode("303"); if (product == null) { Product pr1 = new Product("303", "Term Loan"); productRepository.save(product); };
            product = productRepository.findByCode("304"); if (product == null) { Product pr1 = new Product("304", "Debentures"); productRepository.save(product); };
            product = productRepository.findByCode("305"); if (product == null) { Product pr1 = new Product("305", "Non Fund Based Loan"); productRepository.save(product); };
            product = productRepository.findByCode("30F"); if (product == null) { Product pr1 = new Product("30F", "Facilities"); productRepository.save(product); };
            product = productRepository.findByCode("310"); if (product == null) { Product pr1 = new Product("310", "Facilities Drawdown-NFB Loan"); productRepository.save(product); };
            product = productRepository.findByCode("311"); if (product == null) { Product pr1 = new Product("311", "Facilities Drawdown-Term Loan"); productRepository.save(product); };
            product = productRepository.findByCode("320"); if (product == null) { Product pr1 = new Product("320", "SME Term Loan"); productRepository.save(product); };
            product = productRepository.findByCode("321"); if (product == null) { Product pr1 = new Product("321", "FI Term Loan"); productRepository.save(product); };
            product = productRepository.findByCode("991"); if (product == null) { Product pr1 = new Product("991", "Short Term Loan for Vehicle"); productRepository.save(product); };

        return;
    }
}