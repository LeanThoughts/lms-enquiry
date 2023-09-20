package pfs.lms.enquiry.boardapproval.rejectedbycustomer;

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
public class BoardApprovalRejectedByCustomerController {

    private final BoardApprovalRejectedByCustomerService service;

    @PostMapping("/boardApprovalRejectedByCustomers/create")
    public ResponseEntity<BoardApprovalRejectedByCustomer> create(@RequestBody BoardApprovalRejectedByCustomerResource resource,
                                                                  HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/boardApprovalRejectedByCustomers/update")
    public ResponseEntity<BoardApprovalRejectedByCustomer> update(@RequestBody BoardApprovalRejectedByCustomerResource resource,
                                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/boardApprovalRejectedByCustomers/delete/{id}")
    public ResponseEntity<BoardApprovalRejectedByCustomer> update(@PathVariable("id") UUID id,
                                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id));
    }
}
