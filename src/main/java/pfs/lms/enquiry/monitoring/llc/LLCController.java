package pfs.lms.enquiry.monitoring.llc;

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
public class LLCController {

    private final ILLCService llcService;

    @PostMapping("/loanApplications/lendersLegalCouncils/create")
    public ResponseEntity createLLC(@RequestBody LLCResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersLegalCouncil lendersLegalCouncil =
                llcService.saveLLC(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersLegalCouncil);
    }

    @PutMapping("/loanApplications/lendersLegalCouncils/{id}")
    public ResponseEntity updateLLC(@PathVariable("id") String liaId, @RequestBody LLCResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        LendersLegalCouncil lendersLegalCouncil =
                llcService.updateLLC(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersLegalCouncil);
    }

    @GetMapping("/loanApplications/{loanapplicationid}/lendersLegalCouncils")
    public ResponseEntity getLendersLegalCouncils(@PathVariable("loanapplicationid") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<LLCResource> list = llcService.getLendersLegalCouncils(loanApplicationId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/loanApplications/llcReportAndFeeSubmission/create")
    public ResponseEntity createLLCReportSubmissionAndFee(@RequestBody LLCReportAndFeeResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        LLCReportAndFee llcReportAndFee = llcService.saveLLCReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(llcReportAndFee);
    }

    @PutMapping("/loanApplications/llcReportAndFeeSubmission/{id}")
    public ResponseEntity updateLLCReportSubmissionAndFee(@PathVariable("id") String liaId, @RequestBody LLCReportAndFeeResource resource,
                                                          HttpServletRequest request) throws CloneNotSupportedException {
        LLCReportAndFee llcReportAndFee =
                llcService.updateLLCReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(llcReportAndFee);
    }

    @GetMapping("/loanApplications/llcReportAndFeeSubmission/{liaId}/llcReceiptsAndFees")
    public ResponseEntity getLIAReportAndFee(@PathVariable("liaId") String liaId, HttpServletRequest request)
    {
        List<LLCReportAndFeeResource> llcReportAndFeeResources = llcService.getLLCReportAndFee(liaId);
        return ResponseEntity.ok(llcReportAndFeeResources);
    }

    @DeleteMapping("/lendersLegalCouncils/delete/{id}/moduleName/{moduleName}")
    public ResponseEntity<LendersLegalCouncil> deleteLLC(@PathVariable("id") UUID llcId,
                                                         @PathVariable("moduleName") String moduleName,
                                                         HttpServletRequest request)
            throws CloneNotSupportedException {
        LendersLegalCouncil lendersLegalCouncil = llcService.deleteLLC(llcId, moduleName,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(lendersLegalCouncil);
    }

    @DeleteMapping("/lLCReportAndFees/delete/{id}/moduleName/{moduleName}")
    public ResponseEntity<LLCReportAndFee> deleteLLCReportAndFee(@PathVariable("id") UUID llcReportAndFeeId,
                                                                 @PathVariable("moduleName") String moduleName,
                                                                 HttpServletRequest request)
            throws CloneNotSupportedException {
        LLCReportAndFee llcReportAndFee = llcService.deleteLLCReportAndFee(llcReportAndFeeId, moduleName,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(llcReportAndFee);
    }
}
