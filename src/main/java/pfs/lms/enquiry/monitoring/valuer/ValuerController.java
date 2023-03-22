package pfs.lms.enquiry.monitoring.valuer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ValuerController {

    private final IValuerService valuerService;

    @PostMapping("/loanApplications/valuers/create")
    public ResponseEntity createValuer(@RequestBody ValuerResource resource, HttpServletRequest request)
                                    throws CloneNotSupportedException {
        Valuer valuer = valuerService.saveValuer(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(valuer);
    }

    @PutMapping("/loanApplications/valuers/{id}")
    public ResponseEntity updateLLC(@PathVariable("id") String valuerId, @RequestBody ValuerResource resource,
                                    HttpServletRequest request) throws CloneNotSupportedException {
        Valuer valuer = valuerService.updateValuer(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(valuer);
    }

    @GetMapping("/loanApplications/{loanapplicationid}/valuers")
    public ResponseEntity getLendersLegalCouncils(@PathVariable("loanapplicationid") String loanApplicationId,
                                     HttpServletRequest request)
    {
        List<ValuerResource> list = valuerService.getValuers(loanApplicationId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/loanApplications/valuerReportAndFeeSubmission/create")
    public ResponseEntity createValuerReportSubmissionAndFee(@RequestBody ValuerReportAndFeeResource resource,
                                                             HttpServletRequest request)
            throws CloneNotSupportedException {
        ValuerReportAndFee valuerReportAndFee = valuerService.saveValuerReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(valuerReportAndFee);
    }

    @PutMapping("/loanApplications/valuerReportAndFeeSubmission/{id}")
    public ResponseEntity updateValuerReportSubmissionAndFee(@PathVariable("id") String valuerReportAndFeeId, @RequestBody ValuerReportAndFeeResource resource,
                                                          HttpServletRequest request) throws CloneNotSupportedException {
        ValuerReportAndFee valuerReportAndFee =
                valuerService.updateValuerReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(valuerReportAndFee);
    }

    @GetMapping("/loanApplications/valuerReportAndFeeSubmission/{valuerId}/valuerReceiptsAndFees")
    public ResponseEntity getLIAReportAndFee(@PathVariable("valuerId") String valuerId, HttpServletRequest request)
    {
        List<ValuerReportAndFeeResource> valuerReportAndFeeResources = valuerService.getValuerReportAndFees(valuerId);
        return ResponseEntity.ok(valuerReportAndFeeResources);
    }
}
