package pfs.lms.enquiry.appraisal.riskreport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RiskClientController {

    private final IRiskClient riskClient;

    @GetMapping("/loanApplications/riskModelSummary/{loanContractId}")
    public ResponseEntity<List<RiskEvaluationSummary>> findRiskModelSummaryForLoanContractId(
            @PathVariable String loanContractId, HttpServletRequest request) {
        return ResponseEntity.ok(riskClient.findRiskModelSummaryForLoanContractId(loanContractId));
    }
}
