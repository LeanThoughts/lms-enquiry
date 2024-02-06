package pfs.lms.enquiry.documentation.nodalofficer;

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
public class NodalOfficerController {

    private final NodalOfficerService service;

    @PostMapping("/nodalOfficers/create")
    public ResponseEntity<NodalOfficer> create(@RequestBody NodalOfficerResource resource, HttpServletRequest request) {
        return ResponseEntity.ok(service.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/nodalOfficers/update")
    public ResponseEntity<NodalOfficer> update(@RequestBody NodalOfficerResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        return ResponseEntity.ok(service.update(resource, request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/nodalOfficers/delete/{id}")
    public ResponseEntity<NodalOfficer> delete(@PathVariable("id") UUID id,
                                               HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(service.delete(id, request.getUserPrincipal().getName()));
    }
}
