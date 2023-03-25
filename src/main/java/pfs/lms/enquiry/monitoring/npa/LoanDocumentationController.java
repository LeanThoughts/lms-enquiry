package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.monitoring.loanDocumentation.ILoanDocumentationService;
import pfs.lms.enquiry.monitoring.loanDocumentation.LoanDocumentation;
import pfs.lms.enquiry.monitoring.loanDocumentation.LoanDocumentationResource;
import pfs.lms.enquiry.monitoring.npa.NPADetail;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RepositoryRestController
@AllArgsConstructor
public class LoanDocumentationController {

    private final ILoanDocumentationService loanDocumentationService;

    @PostMapping("/loanDocumentation/create")
    public ResponseEntity<LoanDocumentation> createLoanDocumentation(@RequestBody LoanDocumentationResource resource, HttpServletRequest request) {
        LoanDocumentation loanDocumentation  = loanDocumentationService.saveLoanDocumentation(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentation);
    }

    @PutMapping("/loanDocumentation/{id}")
    public ResponseEntity<LoanDocumentation> updateLoanDocumentation(@PathVariable("id") String npaId,
                                                    @RequestBody LoanDocumentationResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        LoanDocumentation loanDocumentation = loanDocumentationService.updateLoanDocumentation(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentation);
    }

    @GetMapping("/cla/loanDocumentation/loanApplication/{loanapplicationid}")
    public ResponseEntity<List<LoanDocumentation>> getLoanDocumentation(@PathVariable("loanapplicationid") String loanApplicationId, HttpServletRequest request) {

        List<LoanDocumentation> loanDocumentations = loanDocumentationService.getLoanDocumentation(loanApplicationId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentations);
    }

    @GetMapping("/loanDocumentation")
    public ResponseEntity<LoanDocumentation> getLoanDocumentation( HttpServletRequest request) {

        LoanDocumentation loanDocumentation = new LoanDocumentation(); // loanDocumentationService.getLoanDocumentation(loanApplicationId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentation);
    }

}
