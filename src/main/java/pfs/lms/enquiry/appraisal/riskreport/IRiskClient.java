package pfs.lms.enquiry.appraisal.riskreport;

import javassist.bytecode.ByteArray;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "riskRating", url = "${riskRating.baseUrl}")  // http://localhost:8090/risk/api
public interface IRiskClient {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/riskEvaluationSummary/loanContractId/{loanContractId}",
                    method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    List<RiskEvaluationSummary> findRiskModelSummaryForLoanContractId(
            @PathVariable("loanContractId") String loanContractId);


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/riskModelPDF/external",
            method = RequestMethod.GET,consumes = "application/pdf")
    ResponseEntity<ByteArray> printRiskReport(@RequestParam(value = "id", required = true) Long id);




}
