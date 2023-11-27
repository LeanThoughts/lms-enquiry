package pfs.lms.enquiry.iccapproval.rejectedbycustomer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class RejectedByCustomerController {

    private final IRejectedByCustomerService rejectedByCustomerService;

    @PostMapping("/rejectedByCustomers/create")
    public ResponseEntity<RejectedByCustomer> create(@RequestBody RejectedByCustomerResource rejectedByCustomerResource,
                                                     HttpServletRequest request) {

        return ResponseEntity.ok(rejectedByCustomerService.create(rejectedByCustomerResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/rejectedByCustomers/update")
    public ResponseEntity<RejectedByCustomer> update(@RequestBody RejectedByCustomerResource rejectedByCustomerResource,
                                                     HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(rejectedByCustomerService.update(rejectedByCustomerResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/rejectedByCustomers/delete/{id}")
    public ResponseEntity<RejectedByCustomer> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        RejectedByCustomer rejectedByCustomer = rejectedByCustomerService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(rejectedByCustomer);
    }
}
