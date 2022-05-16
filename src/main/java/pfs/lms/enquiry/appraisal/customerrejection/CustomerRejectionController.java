package pfs.lms.enquiry.appraisal.customerrejection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class CustomerRejectionController {

    private final CustomerRejectionService customerRejectionService;

    @PostMapping("/customerRejections/create")
    public ResponseEntity<CustomerRejection> createCustomerRejection(@RequestBody CustomerRejectionResource customerRejectionResource,
                HttpServletRequest request) {
        return ResponseEntity.ok(customerRejectionService.createCustomerRejection(customerRejectionResource,request.getUserPrincipal().getName()));
    }

    @PutMapping("/customerRejections/update")
    public ResponseEntity<CustomerRejection> updateCustomerRejection(@RequestBody CustomerRejectionResource customerRejectionResource,
                HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(customerRejectionService.updateCustomerRejection(customerRejectionResource,request.getUserPrincipal().getName()));
    }
}
