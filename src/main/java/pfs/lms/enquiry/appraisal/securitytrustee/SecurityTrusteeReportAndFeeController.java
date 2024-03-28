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
public class SecurityTrusteeReportAndFeeController
{
    private final SecurityTrusteeReportAndFeeService service;

    @PostMapping("/securityTrusteeReportAndFees/create")
    public ResponseEntity<SecurityTrusteeReportAndFee> create(@RequestBody SecurityTrusteeReportAndFeeResource resource,
                                                              HttpServletRequest request) throws CloneNotSupportedException
    {
        SecurityTrusteeReportAndFee reportAndFee = service.saveReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(reportAndFee);
    }

    @PutMapping("/securityTrusteeReportAndFees/update/{id}")
    public ResponseEntity<SecurityTrusteeReportAndFee> update(@PathVariable("id") String id, @RequestBody SecurityTrusteeReportAndFeeResource resource,
                                                              HttpServletRequest request) throws CloneNotSupportedException
    {
        SecurityTrusteeReportAndFee reportAndFee = service.updateReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(reportAndFee);
    }

    @DeleteMapping("/securityTrusteeReportAndFees/{id}/moduleName/{moduleName}")
    public ResponseEntity<SecurityTrusteeReportAndFee> delete(@PathVariable("id") UUID id, @PathVariable("moduleName") String moduleName,
                                                  HttpServletRequest request) throws CloneNotSupportedException
    {
        SecurityTrusteeReportAndFee reportAndFee = service.deleteReportAndFee(id, moduleName, request.getUserPrincipal().getName());
        return ResponseEntity.ok(reportAndFee);
    }

    @GetMapping("/securityTrustees/{securityTrusteeId}/securityTrusteeReportAndFees")
    public ResponseEntity<List<SecurityTrusteeReportAndFeeResource>> getReportAndFees(@PathVariable("securityTrusteeId") String securityTrusteeId,
                                                                                      HttpServletRequest request)
    {
        List<SecurityTrusteeReportAndFeeResource> reportAndFees = service.getReportAndFees(securityTrusteeId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(reportAndFees);
    }
}
