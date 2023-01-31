package pfs.lms.enquiry.monitoring.lia;

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
public class LIAController {

    private final ILIAService liaService;

    @PostMapping("/loanApplications/lendersInsuranceAdvisors/create")
    public ResponseEntity createLIA(@RequestBody LIAResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersInsuranceAdvisor lendersInsuranceAdvisor =
                liaService.saveLIA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersInsuranceAdvisor);
    }

    @PutMapping("/loanApplications/lendersInsuranceAdvisors/{id}")
    public ResponseEntity updateLIA(@PathVariable("id") String liaId, @RequestBody LIAResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersInsuranceAdvisor lendersInsuranceAdvisor =
                liaService.updateLIA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersInsuranceAdvisor);
    }

    @GetMapping("/loanApplications/{loanapplicationid}/lendersInsuranceAdvisors")
    public ResponseEntity getLendersInsuranceAdvisors(@PathVariable("loanapplicationid") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<LIAResource> list = liaService.getLendersInsuranceAdvisors(loanApplicationId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/loanApplications/liaReportAndFeeSubmission/create")
    public ResponseEntity createLIAReportSubmissionAndFee(@RequestBody LIAReportAndFeeResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        LIAReportAndFee liaReportAndFee = liaService.saveLIAReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(liaReportAndFee);
    }

    @PutMapping("/loanApplications/liaReportAndFeeSubmission/{id}")
    public ResponseEntity updateLIAReportSubmissionAndFee(@PathVariable("id") String liaId, @RequestBody LIAReportAndFeeResource resource,
                                                          HttpServletRequest request) throws CloneNotSupportedException {
        LIAReportAndFee liaReportAndFee =
                liaService.updateLIAReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(liaReportAndFee);
    }

    @GetMapping("/loanApplications/liaReportAndFeeSubmission/{liaId}/liaReceiptsAndFees")
    public ResponseEntity getLIAReportAndFee(@PathVariable("liaId") String liaId, HttpServletRequest request)
    {
        List<LIAReportAndFeeResource>  liaReportAndFeeResources = liaService.getLIAReportAndFee(liaId);
        return ResponseEntity.ok(liaReportAndFeeResources);
    }
}
