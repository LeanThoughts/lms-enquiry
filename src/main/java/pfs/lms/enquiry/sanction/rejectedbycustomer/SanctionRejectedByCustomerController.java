package pfs.lms.enquiry.sanction.rejectedbycustomer;

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
public class SanctionRejectedByCustomerController {

    private final SanctionRejectedByCustomerService service;

    @PostMapping("/sanctionRejectedByCustomers/create")
    public ResponseEntity<SanctionRejectedByCustomer> create(@RequestBody SanctionRejectedByCustomerResource resource,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/sanctionRejectedByCustomers/update")
    public ResponseEntity<SanctionRejectedByCustomer> update(@RequestBody SanctionRejectedByCustomerResource resource,
                                                             HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/sanctionRejectedByCustomers/delete/{id}")
    public ResponseEntity<SanctionRejectedByCustomer> update(@PathVariable("id") UUID id,
                                                             HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id));
    }
}
