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
public class ExternalRatingController {

    private final IExternalRatingService externalRatingService;

    @PostMapping("/externalRatings/create")
    public ResponseEntity<ExternalRating> createExternalRating(@RequestBody ExternalRatingResource resource,
                                                               HttpServletRequest request)
    {
        ExternalRating externalRating = externalRatingService.createExternalRating(resource,request.getUserPrincipal().getName());
        return ResponseEntity.ok(externalRating);
    }

    @PutMapping("/externalRatings/update")
    public ResponseEntity<ExternalRating> updateExternalRating(@RequestBody ExternalRatingResource resource,
                                                               HttpServletRequest request) throws CloneNotSupportedException
    {
        ExternalRating externalRating = externalRatingService.updateExternalRating(resource, request.getUserPrincipal().getName());
        return ResponseEntity.ok(externalRating);
    }

    @DeleteMapping("/externalRatings/delete/{id}")
    public ResponseEntity<ExternalRating> deleteExternalRating(@PathVariable("id") UUID ratingId, HttpServletRequest request)
    {
        ExternalRating externalRating = externalRatingService.deleteExternalRating(ratingId, request.getUserPrincipal().getName());
        return ResponseEntity.ok(externalRating);
    }
}
