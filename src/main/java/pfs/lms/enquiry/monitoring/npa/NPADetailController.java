package pfs.lms.enquiry.monitoring.npa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RepositoryRestController
@AllArgsConstructor
public class NPADetailController {

    private final INPADetailService npaDetailService;

    @PostMapping("/nPADetails/create")
    public ResponseEntity<NPADetail> createNPADetail(@RequestBody NPADetailResource resource,
                                                     HttpServletRequest request) {
        NPADetail npaDetail = npaDetailService.saveNPADetail(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(npaDetail);
    }

    @PutMapping("/nPADetails/update")
    public ResponseEntity<NPADetail> updateNPADetail(@RequestBody NPADetailResource resource,
                                                HttpServletRequest request) throws CloneNotSupportedException {
        NPADetail npaDetail = npaDetailService.updateNPADetail(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(npaDetail);
    }

    @GetMapping("/nPADetails/npaId/{npaId}")
    public ResponseEntity<List<NPADetail>> getNPADetails(@PathVariable("npaId")
                                                     String npaId, HttpServletRequest request) {
        List<NPADetail> npaDetails = npaDetailService.getNPADetail(npaId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(npaDetails);
    }
}
