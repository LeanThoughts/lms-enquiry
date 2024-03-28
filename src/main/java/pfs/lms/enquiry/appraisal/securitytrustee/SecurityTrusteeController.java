package pfs.lms.enquiry.appraisal.securitytrustee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
public class SecurityTrusteeController
{
    private final SecurityTrusteeService securityTrusteeService;

    @PostMapping("/securityTrustees/create")
    public ResponseEntity<SecurityTrustee> createSecurityTrustee(@RequestBody SecurityTrusteeResource resource, HttpServletRequest request)
            throws CloneNotSupportedException
    {
        SecurityTrustee securityTrustee = securityTrusteeService.saveSecurityTrustee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(securityTrustee);
    }

    @PutMapping("/securityTrustees/update/{id}")
    public ResponseEntity<SecurityTrustee> updateSecurityTrustee(@PathVariable("id") String id, @RequestBody SecurityTrusteeResource resource,
                                                                 HttpServletRequest request) throws CloneNotSupportedException
    {
        SecurityTrustee securityTrustee = securityTrusteeService.updateSecurityTrustee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(securityTrustee);
    }

    @DeleteMapping("/securityTrustees/{id}/moduleName/{moduleName}")
    public ResponseEntity<SecurityTrustee> deleteSecurityTrustee(@PathVariable("id") UUID id, @PathVariable("moduleName") String moduleName,
                                                                            HttpServletRequest request) throws CloneNotSupportedException
    {
        SecurityTrustee securityTrustee = securityTrusteeService.deleteSecurityTrustee(id, moduleName, request.getUserPrincipal().getName());
        return ResponseEntity.ok(securityTrustee);
    }

    @GetMapping("/loanApplications/{loanApplicationId}/securityTrustees")
    public ResponseEntity<List<SecurityTrusteeResource>> getSecurityTrustees(@PathVariable("loanApplicationId") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<SecurityTrusteeResource> securityTrusteeResources = securityTrusteeService.getSecurityTrustees(loanApplicationId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(securityTrusteeResources);
    }
}
