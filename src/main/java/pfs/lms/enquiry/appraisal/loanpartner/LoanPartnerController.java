package pfs.lms.enquiry.appraisal.loanpartner;

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
public class LoanPartnerController {

    private final ILoanPartnerService loanPartnerService;

    @PostMapping("/loanPartners/create")
    public ResponseEntity<LoanPartner> createLoanPartner(@RequestBody LoanPartnerResource loanPartnerResource,
                                                         HttpServletRequest request) {
        LoanPartner loanPartner = loanPartnerService.createLoanPartner(loanPartnerResource);
        return ResponseEntity.ok(loanPartner);
    }

    @PutMapping("/loanPartners/update")
    public ResponseEntity<LoanPartner> updateLoanPartner(@RequestBody LoanPartnerResource loanPartnerResource,
                                                         HttpServletRequest request) {
        LoanPartner loanPartner = loanPartnerService.updateLoanPartner((loanPartnerResource));
        return ResponseEntity.ok(loanPartner);
    }

    @DeleteMapping("/loanPartners/delete/{id}")
    public ResponseEntity<LoanPartner> deleteLoanPartner(@PathVariable("id") UUID loanPartnerId) {
        LoanPartner loanPartner = loanPartnerService.deleteLoanPartner(loanPartnerId);
        return ResponseEntity.ok(loanPartner);
    }
}
