package pfs.lms.enquiry.appraisal.riskreport;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "riskRating", url = "${riskRating.baseUrl}")  // http://localhost:8090/risk/api
public interface IRiskClient {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/riskEvaluationSummary/loanContractId/{loanContractId}",
                    method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    List<RiskEvaluationSummary> findRiskModelSummaryForLoanContractId(
            @PathVariable("loanContractId") String loanContractId);
}
