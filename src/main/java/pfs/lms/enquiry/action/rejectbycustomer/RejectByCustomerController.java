package pfs.lms.enquiry.action.rejectbycustomer;

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
public class RejectByCustomerController {

    private final RejectByCustomerService rejectByCustomerService;

    @PostMapping("/rejectByCustomers/create")
    public ResponseEntity<RejectByCustomer> createRejectByCustomerService(@RequestBody RejectByCustomerResource resource,
                                                              HttpServletRequest request) {
        return ResponseEntity.ok(rejectByCustomerService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/rejectByCustomers/update")
    public ResponseEntity<RejectByCustomer> updateRejectByCustomerService(@RequestBody RejectByCustomerResource resource,
                                                              HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(rejectByCustomerService.update(resource, request.getUserPrincipal().getName()));
    }
}
