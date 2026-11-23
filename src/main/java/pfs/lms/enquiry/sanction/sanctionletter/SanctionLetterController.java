package pfs.lms.enquiry.sanction.sanctionletter;

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
public class SanctionLetterController {

    private final SanctionLetterService service;

    @PreAuthorize("hasAuthority('LoanContracts.Sanction.Edit')")
    @PostMapping("/sanctionLetters/create")
    public ResponseEntity<SanctionLetter> create(@RequestBody SanctionLetterResource resource,
                                                 HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.Sanction.Edit')")
    @PutMapping("/sanctionLetters/update")
    public ResponseEntity<SanctionLetter> update(@RequestBody SanctionLetterResource resource,
                                                 HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @PreAuthorize("hasAuthority('LoanContracts.Sanction.Edit')")
    @DeleteMapping("/sanctionLetters/delete/{id}")
    public ResponseEntity<SanctionLetter> delete(@PathVariable("id") UUID id,
                                                 HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id,request.getUserPrincipal().getName()));
    }
}
