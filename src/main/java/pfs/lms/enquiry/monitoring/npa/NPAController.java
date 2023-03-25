package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.monitoring.loanDocumentation.ILoanDocumentationService;
import pfs.lms.enquiry.monitoring.loanDocumentation.LoanDocumentation;
import pfs.lms.enquiry.monitoring.loanDocumentation.LoanDocumentationResource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RepositoryRestController
@AllArgsConstructor
public class NPAController {

    private final INPAService npaService;
    private final ILoanDocumentationService loanDocumentationService;

    @PostMapping("/nPAs/create")
    public ResponseEntity<NPA> createNPA(@RequestBody NPAResource resource, HttpServletRequest request) {
        NPA npa = npaService.saveNPA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(npa);
    }

    @PutMapping("/nPAs/{id}")
    public ResponseEntity<NPA> updateNPA(@PathVariable("id") String npaId,
                                                    @RequestBody NPAResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        NPA npa = npaService.updateNPA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(npa);
    }

    @GetMapping("/nPAs/loanApplication/{loanapplicationid}")
    public ResponseEntity<NPA> getNPA(@PathVariable("loanapplicationid") String loanApplicationId, HttpServletRequest request) {

        NPA npa = npaService.getNPA(loanApplicationId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(npa);
    }

    @GetMapping("/nPAs/loanDocumentation")
    public ResponseEntity<LoanDocumentation> getLoanDocumentation(HttpServletRequest request) {

        LoanDocumentation loanDocumentation = new LoanDocumentation(); // loanDocumentationService.getLoanDocumentation(loanApplicationId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentation);
    }
    @GetMapping("/nPAs/loanDocumentation/loanApplication/{loanapplicationid}")
    public ResponseEntity<List<LoanDocumentation>> getLoanDocumentation(@PathVariable("loanapplicationid") String loanApplicationId, HttpServletRequest request) {

        List<LoanDocumentation> loanDocumentations = loanDocumentationService.getLoanDocumentation(loanApplicationId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentations);
    }

    @PostMapping("/nPAs/loanDocumentation/create")
    public ResponseEntity<LoanDocumentation> createLoanDocumentation(@RequestBody LoanDocumentationResource resource, HttpServletRequest request) {
        LoanDocumentation loanDocumentation  = loanDocumentationService.saveLoanDocumentation(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentation);
    }

    @PutMapping("/nPAs/loanDocumentation/{id}")
    public ResponseEntity<LoanDocumentation> updateLoanDocumentation(@PathVariable("id") String npaId,
                                                                     @RequestBody LoanDocumentationResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        LoanDocumentation loanDocumentation = loanDocumentationService.updateLoanDocumentation(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanDocumentation);
    }
}
