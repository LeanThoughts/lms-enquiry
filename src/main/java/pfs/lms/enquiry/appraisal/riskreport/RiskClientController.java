package pfs.lms.enquiry.appraisal.riskreport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pfs.lms.enquiry.config.ApiController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RepositoryRestController
@ApiController

public class RiskClientController {

    private final IRiskClient riskClient;

    @GetMapping("/test")
    public ResponseEntity test( HttpServletRequest request) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/loanApplications/riskModelSummary/{loanContractId}")
    public ResponseEntity<List<RiskEvaluationSummary>> findRiskModelSummaryForLoanContractId(
            @PathVariable String loanContractId, HttpServletRequest request) {
        return ResponseEntity.ok(riskClient.findRiskModelSummaryForLoanContractId(loanContractId));
    }
    @GetMapping("/riskModelPDF")
    public ResponseEntity getPdf(
            @RequestParam(value = "id", required = true) Long id) {

        ResponseEntity r = (ResponseEntity) riskClient.printRiskReport(id);

        return ResponseEntity.ok(r);
    }

}
