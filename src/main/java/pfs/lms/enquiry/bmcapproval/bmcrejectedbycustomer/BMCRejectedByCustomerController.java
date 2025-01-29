package pfs.lms.enquiry.bmcapproval.bmcrejectedbycustomer;

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
public class BMCRejectedByCustomerController {

    private final IBMCRejectedByCustomerService ibmcRejectedByCustomerService;

    @PostMapping("/bmcRejectedByCustomers/create")
    public ResponseEntity<BmcRejectedByCustomer> create(@RequestBody BMCRejectedByCustomerResource bmcRejectedByCustomerResource,
                                                        HttpServletRequest request) {

        return ResponseEntity.ok(ibmcRejectedByCustomerService.create(bmcRejectedByCustomerResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/bmcRejectedByCustomers/update")
    public ResponseEntity<BmcRejectedByCustomer> update(@RequestBody BMCRejectedByCustomerResource bmcRejectedByCustomerResource,
                                                        HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(ibmcRejectedByCustomerService.update(bmcRejectedByCustomerResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/bmcRejectedByCustomers/delete/{id}")
    public ResponseEntity<BmcRejectedByCustomer> delete(@PathVariable("id") UUID iccFurtherDetailId, HttpServletRequest request) {
        BmcRejectedByCustomer BMCRejectedByCustomer = ibmcRejectedByCustomerService.delete(iccFurtherDetailId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(BMCRejectedByCustomer);
    }
}
