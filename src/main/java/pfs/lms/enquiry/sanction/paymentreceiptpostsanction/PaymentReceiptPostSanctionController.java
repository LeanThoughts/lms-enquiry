package pfs.lms.enquiry.sanction.paymentreceiptpostsanction;

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
public class PaymentReceiptPostSanctionController {

    private final PaymentReceiptPostSanctionService service;

    @PostMapping("/paymentReceiptPostSanctions/create")
    public ResponseEntity<PaymentReceiptPostSanction> createPaymentReceipt(@RequestBody PaymentReceiptPostSanctionResource resource,
                                                                           HttpServletRequest request) {
        return ResponseEntity.ok(service.createPaymentReceipt(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/paymentReceiptPostSanctions/update")
    public ResponseEntity<PaymentReceiptPostSanction> updatePaymentReceipt(@RequestBody PaymentReceiptPostSanctionResource resource,
                                                                           HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.updatePaymentReceipt(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/paymentReceiptPostSanctions/delete/{id}")
    public ResponseEntity<PaymentReceiptPostSanction> updatePaymentReceipt(@PathVariable("id") UUID id,
                                                                           HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.deletePaymentReceipt(id, request.getUserPrincipal().getName()));
    }
}
