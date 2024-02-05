package pfs.lms.enquiry.documentation.legalcounselfee;

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
public class LegalCounselFeeController {

    private final LegalCounselFeeService service;

    @PostMapping("/legalCounselFees/create")
    public ResponseEntity<LegalCounselFee> create(@RequestBody LegalCounselFeeResource resource, HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/legalCounselFees/update")
    public ResponseEntity<LegalCounselFee> update(@RequestBody LegalCounselFeeResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/legalCounselFees/delete/{id}")
    public ResponseEntity<LegalCounselFee> delete(@PathVariable("id") UUID id,
                                                  HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id, request.getUserPrincipal().getName()));
    }
}
