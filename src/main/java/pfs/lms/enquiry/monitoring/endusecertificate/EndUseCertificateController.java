package pfs.lms.enquiry.monitoring.endusecertificate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class EndUseCertificateController {

    private final IEndUseCertificateService endUseCertificateService;

    @PostMapping("/endUseCertificates/create")
    public ResponseEntity<EndUseCertificate> saveEndUseCertificate(@RequestBody EndUseCertificateResource resource,
                                                                   HttpServletRequest request)
            throws CloneNotSupportedException {
        EndUseCertificate endUseCertificate = endUseCertificateService.saveEndUseCertificate(resource,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(endUseCertificate);
    }

    @PutMapping("/endUseCertificates/update")
    public ResponseEntity<EndUseCertificate> updateEndUseCertificate(@RequestBody EndUseCertificateResource resource,
                                                                     HttpServletRequest request)
            throws CloneNotSupportedException {
        EndUseCertificate endUseCertificate = endUseCertificateService.updateEndUseCertificate(resource,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(endUseCertificate);
    }

    @DeleteMapping("/endUseCertificates/delete/{id}")
    public ResponseEntity<EndUseCertificate> deleteEndUseCertificate(@PathVariable("id") UUID endUseCertificateId,
                                                                     HttpServletRequest request)
            throws CloneNotSupportedException {
        EndUseCertificate endUseCertificate = endUseCertificateService.deleteEndUseCertificate(endUseCertificateId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(endUseCertificate);
    }

    @GetMapping("/endUseCertificates/loanApplicationId/{loanApplicationId}")
    public ResponseEntity<List<EndUseCertificate>> getEndUseCertificates(@PathVariable("loanApplicationId")
                                                                                     UUID loanApplicationId,
                                                                         HttpServletRequest request) {
        List<EndUseCertificate> endUseCertificates = endUseCertificateService.getEndUseCertificates(loanApplicationId);
        return ResponseEntity.ok(endUseCertificates);
    }
}
