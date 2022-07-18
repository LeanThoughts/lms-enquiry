package pfs.lms.enquiry.appraisal.riskrating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class CorporateLoanRiskRatingController {

    private final ICorporateLoanRiskRatingService corporateLoanRiskRatingService;

    @PostMapping("/corporateLoanRiskRatings/create")
    public ResponseEntity<CorporateLoanRiskRating> createCorporateLoanRiskRating(
            @RequestBody CorporateLoanRiskRatingResource corporateLoanRiskRatingResource, HttpServletRequest request) {

        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingService.
                createCorporateLoanRiskRating(corporateLoanRiskRatingResource,request.getUserPrincipal().getName());
        return ResponseEntity.ok(corporateLoanRiskRating);
    }

    @PutMapping("/corporateLoanRiskRatings/update")
    public ResponseEntity<CorporateLoanRiskRating> updateCorporateLoanRiskRating(
            @RequestBody CorporateLoanRiskRatingResource corporateLoanRiskRatingResource, HttpServletRequest request)
            throws CloneNotSupportedException {

        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingService.
                updateCorporateLoanRiskRating(corporateLoanRiskRatingResource,request.getUserPrincipal().getName());
        return ResponseEntity.ok(corporateLoanRiskRating);
    }

    @DeleteMapping("/corporateLoanRiskRatings/delete/{id}")
    public ResponseEntity<CorporateLoanRiskRating> deleteCorporateLoanRiskRating(@PathVariable("id") UUID ratingId, HttpServletRequest request) {
        CorporateLoanRiskRating corporateLoanRiskRating = corporateLoanRiskRatingService.deleteCorporateLoanRiskRating(ratingId,request.getUserPrincipal().getName());
        return ResponseEntity.ok(corporateLoanRiskRating);
    }
}
