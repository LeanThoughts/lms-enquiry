package pfs.lms.enquiry.applicationfee.termsheet;

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
public class TermSheetController {

    private final ITermSheetService termSheetService;

    @PostMapping("/termSheets/create")
    public ResponseEntity<TermSheet> create(@RequestBody TermSheetResource termSheetResource,
                                            HttpServletRequest request) {

        return ResponseEntity.ok(termSheetService.create(termSheetResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/termSheets/update")
    public ResponseEntity<TermSheet> update(@RequestBody TermSheetResource termSheetResource,
                                            HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(termSheetService.update(termSheetResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/termSheets/delete/{id}")
    public ResponseEntity<TermSheet> delete(@PathVariable("id") UUID loanEnhancementId, HttpServletRequest request) {
        TermSheet termSheet = termSheetService.delete(loanEnhancementId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(termSheet);
    }
}
