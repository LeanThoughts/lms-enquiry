package pfs.lms.enquiry.appraisal.riskrating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfs.lms.enquiry.appraisal.riskreport.IRiskClient;
import pfs.lms.enquiry.appraisal.riskreport.RiskEvaluationSummary;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class TermLoanRiskRatingController {

    private final TermLoanRiskRatingService termLoanRiskRatingService;

    private final IRiskClient riskClient;

    @PostMapping("/termLoanRiskRatings/create")
    public ResponseEntity<TermLoanRiskRating> createTermLoanRiskRating(
            @RequestBody TermLoanRiskRatingResource termLoanRiskRatingResource, HttpServletRequest request) {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingService.createTermLoanRiskRating(termLoanRiskRatingResource);
        return ResponseEntity.ok(termLoanRiskRating);
    }

    @PutMapping("/termLoanRiskRatings/update")
    public ResponseEntity<TermLoanRiskRating> updateTermLoanRiskRating(
            @RequestBody TermLoanRiskRatingResource termLoanRiskRatingResource, HttpServletRequest request) throws CloneNotSupportedException {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingService.updateTermLoanRiskRating(termLoanRiskRatingResource);
        return ResponseEntity.ok(termLoanRiskRating);
    }

    @DeleteMapping("/termLoanRiskRatings/delete/{id}")
    public ResponseEntity<TermLoanRiskRating> deleteTermLoanRiskRating(@PathVariable("id") UUID ratingId, HttpServletRequest request) {
        TermLoanRiskRating termLoanRiskRating = termLoanRiskRatingService.deleteTermLoanRiskRating(ratingId);
        return ResponseEntity.ok(termLoanRiskRating);
    }

    @GetMapping("/termLoanRiskRatings/riskModelSummary/{loanContractId}")
    public ResponseEntity<List<RiskEvaluationSummary>> findRiskModelSummaryForLoanContractId(
            @PathVariable String loanContractId, HttpServletRequest request) {
        return ResponseEntity.ok(riskClient.findRiskModelSummaryForLoanContractId(loanContractId));
    }
}
