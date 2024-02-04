package pfs.lms.enquiry.documentation.legalcouncelreport;

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
public class LegalCounselReportController {

    private final LegalCounselReportService service;

    @PostMapping("/legalCounselReports/create")
    public ResponseEntity<LegalCounselReport> create(@RequestBody LegalCounselReportResource resource, HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/legalCounselReports/update")
    public ResponseEntity<LegalCounselReport> update(@RequestBody LegalCounselReportResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/legalCounselReports/delete/{id}")
    public ResponseEntity<LegalCounselReport> delete(@PathVariable("id") UUID id,
                                                     HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id, request.getUserPrincipal().getName()));
    }
}
