package pfs.lms.enquiry.applicationfee.termsheet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class TermSheetController {

    private final ITermSheetService termSheetService;

    @PreAuthorize("hasAuthority('LoanContracts.ApplicationFee.Edit')")
    @PostMapping("/termSheets/create")
    public ResponseEntity<TermSheet> create(@RequestBody TermSheetResource termSheetResource,
                                            HttpServletRequest request) {

        return ResponseEntity.ok(termSheetService.create(termSheetResource,
                request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ApplicationFee.Edit')")
    @PutMapping("/termSheets/update")
    public ResponseEntity<TermSheet> update(@RequestBody TermSheetResource termSheetResource,
                                            HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(termSheetService.update(termSheetResource,
                request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.ApplicationFee.Edit')")
    @DeleteMapping("/termSheets/delete/{id}")
    public ResponseEntity<TermSheet> delete(@PathVariable("id") UUID loanEnhancementId, HttpServletRequest request) {
        TermSheet termSheet = termSheetService.delete(loanEnhancementId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(termSheet);
    }
}
