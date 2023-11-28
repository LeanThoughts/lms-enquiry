package pfs.lms.enquiry.iccapproval.loanenhancement;

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
public class LoanEnhancementController {

    private final ILoanEnhancementService loanEnhancementService;

    @PostMapping("/loanEnhancements/create")
    public ResponseEntity<LoanEnhancement> create(@RequestBody LoanEnhancementResource loanEnhancementResource,
                                                  HttpServletRequest request) {

        return ResponseEntity.ok(loanEnhancementService.create(loanEnhancementResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/loanEnhancements/update")
    public ResponseEntity<LoanEnhancement> update(@RequestBody LoanEnhancementResource loanEnhancementResource,
                                                  HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(loanEnhancementService.update(loanEnhancementResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/loanEnhancements/delete/{id}")
    public ResponseEntity<LoanEnhancement> delete(@PathVariable("id") UUID loanEnhancementId, HttpServletRequest request) {
        LoanEnhancement loanEnhancement = loanEnhancementService.delete(loanEnhancementId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(loanEnhancement);
    }
}
