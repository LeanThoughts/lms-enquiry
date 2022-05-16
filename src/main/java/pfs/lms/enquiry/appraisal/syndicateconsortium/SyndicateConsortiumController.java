package pfs.lms.enquiry.appraisal.syndicateconsortium;

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
public class SyndicateConsortiumController {

    private final SyndicateConsortiumService syndicateConsortiumService;

    @PostMapping("/syndicateConsortiums/create")
    public ResponseEntity<SyndicateConsortium> createSyndicateConsortium(
            @RequestBody SyndicateConsortiumResource syndicateConsortiumResource, HttpServletRequest request) {
        SyndicateConsortium syndicateConsortium =
                syndicateConsortiumService.createSyndicateConsortium(syndicateConsortiumResource,request.getUserPrincipal().getName());
        return ResponseEntity.ok(syndicateConsortium);
    }

    @PutMapping("/syndicateConsortiums/update")
    public ResponseEntity<SyndicateConsortium> updateSyndicateConsortium(
            @RequestBody SyndicateConsortiumResource syndicateConsortiumResource, HttpServletRequest request) throws CloneNotSupportedException {
        SyndicateConsortium syndicateConsortium =
                syndicateConsortiumService.updateSyndicateConsortium(syndicateConsortiumResource,request.getUserPrincipal().getName());
        return ResponseEntity.ok(syndicateConsortium);
    }

    @DeleteMapping("/syndicateConsortiums/delete/{id}")
    public ResponseEntity<SyndicateConsortium> deleteSyndicateConsortium(@PathVariable("id") UUID bankId,HttpServletRequest request) {
        SyndicateConsortium syndicateConsortium = syndicateConsortiumService.deleteSyndicateConsortium(bankId,request.getUserPrincipal().getName());
        return ResponseEntity.ok(syndicateConsortium);
    }
}
