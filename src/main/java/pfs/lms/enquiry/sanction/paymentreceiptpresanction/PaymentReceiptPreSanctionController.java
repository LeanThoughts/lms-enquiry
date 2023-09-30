package pfs.lms.enquiry.sanction.paymentreceiptpresanction;

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
public class PaymentReceiptPreSanctionController {

    private final PaymentReceiptPreSanctionService service;

    @PostMapping("/paymentReceiptPreSanctions/create")
    public ResponseEntity<PaymentReceiptPreSanction> createPaymentReceipt(@RequestBody PaymentReceiptPreSanctionResource resource,
                                                                          HttpServletRequest request) {
        return ResponseEntity.ok(service.createPaymentReceipt(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/paymentReceiptPreSanctions/update")
    public ResponseEntity<PaymentReceiptPreSanction> updatePaymentReceipt(@RequestBody PaymentReceiptPreSanctionResource resource,
                                                                          HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.updatePaymentReceipt(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/paymentReceiptPreSanctions/delete/{id}")
    public ResponseEntity<PaymentReceiptPreSanction> deletePaymentReceipt(@PathVariable("id") UUID id,
                                                                          HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.deletePaymentReceipt(id,request.getUserPrincipal().getName()));
    }
}
