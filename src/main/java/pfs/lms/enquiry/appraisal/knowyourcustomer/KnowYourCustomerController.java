package pfs.lms.enquiry.appraisal.knowyourcustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class KnowYourCustomerController {

    private final IKnowYourCustomerService knowYourCustomerService;

    @PutMapping("/knowYourCustomers/update")
    public ResponseEntity<KnowYourCustomer> updateKYC(@RequestBody KnowYourCustomerResource knowYourCustomerResource,
                                                      HttpServletRequest request) {

        KnowYourCustomer knowYourCustomer = knowYourCustomerService.updateKYC(knowYourCustomerResource);
        return ResponseEntity.ok(knowYourCustomer);
    }
}
