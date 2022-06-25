package pfs.lms.enquiry.appraisal.riskreport;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "riskRating", url = "${riskRating.baseUrl}")
public interface IRiskClient {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/api/riskEvaluationSummary/loanContractId/{loanContractId}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    List<RiskEvaluationSummary> findRiskModelSummaryForLoanContractId(
            @PathVariable("loanContractId") String loanContractId,
            @RequestHeader("Authorization") String authorization);
}
