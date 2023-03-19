package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@AllArgsConstructor
public class NPAController {

    private final INPAService npaService;

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
}
