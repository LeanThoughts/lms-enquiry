package pfs.lms.enquiry.monitoring.cla;

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
public class CLAController {

    private final ICLAService claService;

    @PostMapping("/loanApplications/commonLoanAgreements/create")
    public ResponseEntity createCLA(@RequestBody CLAResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        CommonLoanAgreement commonLoanAgreement =
                claService.saveCLA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(commonLoanAgreement);
    }

    @PutMapping("/loanApplications/commonLoanAgreements/{id}")
    public ResponseEntity updateCLA(@PathVariable("id") String claId, @RequestBody CLAResource resource, HttpServletRequest request) throws CloneNotSupportedException {
        CommonLoanAgreement commonLoanAgreement =
                claService.updateCLA(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(commonLoanAgreement);
    }

    @GetMapping("/loanApplications/{loanapplicationid}/commonLoanAgreements")
    public ResponseEntity getCommonLoanAgreements(@PathVariable("loanapplicationid") String loanApplicationId,
                                                         HttpServletRequest request)
    {
        List<CLAResource> list = claService.getCommonLoanAgreements(loanApplicationId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/loanApplications/claReportAndFeeSubmission/create")
    public ResponseEntity createCLAReportSubmissionAndFee(@RequestBody CLAReportAndFeeResource resource, HttpServletRequest request)
            throws CloneNotSupportedException {
        CLAReportAndFee CLAReportAndFee = claService.saveCLAReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(CLAReportAndFee);
    }

    @PutMapping("/loanApplications/claReportAndFeeSubmission/{id}")
    public ResponseEntity updateCLAReportSubmissionAndFee(@PathVariable("id") String claId, @RequestBody CLAReportAndFeeResource resource,
                                                          HttpServletRequest request) throws CloneNotSupportedException {
        CLAReportAndFee CLAReportAndFee =
                claService.updateCLAReportAndFee(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(CLAReportAndFee);
    }

    @GetMapping("/loanApplications/claReportAndFeeSubmission/{claId}/claReceiptsAndFees")
    public ResponseEntity getCLAReportAndFee(@PathVariable("claId") String liaId, HttpServletRequest request)
    {
        List<CLAReportAndFeeResource> claReportAndFeeResources = claService.getCLAReportAndFee(liaId);
        return ResponseEntity.ok(claReportAndFeeResources);
    }
}
