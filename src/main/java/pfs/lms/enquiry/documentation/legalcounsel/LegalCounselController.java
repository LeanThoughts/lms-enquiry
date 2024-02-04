package pfs.lms.enquiry.documentation.legalcounsel;

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
public class LegalCounselController {

    private final LegalCounselService service;

    @PostMapping("/legalCounsels/create")
    public ResponseEntity<LegalCounsel> create(@RequestBody LegalCounselResource resource, HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/legalCounsels/update")
    public ResponseEntity<LegalCounsel> update(@RequestBody LegalCounselResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/legalCounsels/delete/{id}")
    public ResponseEntity<LegalCounsel> delete(@PathVariable("id") UUID id,
                                               HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id, request.getUserPrincipal().getName()));
    }
}
