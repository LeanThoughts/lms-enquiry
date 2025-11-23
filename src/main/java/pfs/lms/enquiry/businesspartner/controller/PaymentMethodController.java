package pfs.lms.enquiry.businesspartner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pfs.lms.enquiry.businesspartner.domain.PaymentMethod;
import pfs.lms.enquiry.businesspartner.repository.PaymentMethodRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodRepository paymentMethodRepository;
    @GetMapping("/paymentmethods")
    public ResponseEntity<List<PaymentMethod>> findAll(HttpServletRequest request) {

        List<PaymentMethod> paymentMethod = paymentMethodRepository.findAll();
        paymentMethod.sort(Comparator.comparing(o ->o.getId()));

        return ResponseEntity.ok(paymentMethod);
    }

}
