package pfs.lms.enquiry.action.projectproposal.creditrating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class CreditRatingController {

    private final ICreditRatingService creditRatingService;

    @PostMapping("/creditRatings/create")
    public ResponseEntity<CreditRating> create(@RequestBody CreditRatingResource resource,
                                               HttpServletRequest request) {
        return ResponseEntity.ok(creditRatingService.create(resource, request.getUserPrincipal().getName()));
    }

    @PutMapping("/creditRatings/update")
    public ResponseEntity<CreditRating> update(@RequestBody CreditRatingResource resource,
                                               HttpServletRequest request) throws CloneNotSupportedException {
        return ResponseEntity.ok(creditRatingService.update(resource, request.getUserPrincipal().getName()));
    }
}
