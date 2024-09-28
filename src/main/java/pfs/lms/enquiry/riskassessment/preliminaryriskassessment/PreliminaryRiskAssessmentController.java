package pfs.lms.enquiry.riskassessment.preliminaryriskassessment;

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
public class PreliminaryRiskAssessmentController {

    private final IPreliminaryRiskAssessmentService preliminaryRiskAssessmentService;

    @PostMapping("/preliminaryRiskAssessments/create")
    public ResponseEntity<PreliminaryRiskAssessment> create(@RequestBody PreliminaryRiskAssessmentResource preliminaryRiskAssessmentResource,
                                                            HttpServletRequest request) {

        return ResponseEntity.ok(preliminaryRiskAssessmentService.create(preliminaryRiskAssessmentResource,
                request.getUserPrincipal().getName()));
    }

    @PutMapping("/preliminaryRiskAssessments/update")
    public ResponseEntity<PreliminaryRiskAssessment> update(@RequestBody PreliminaryRiskAssessmentResource preliminaryRiskAssessmentResource,
                                                            HttpServletRequest request) throws CloneNotSupportedException {

        return ResponseEntity.ok(preliminaryRiskAssessmentService.update(preliminaryRiskAssessmentResource,
                request.getUserPrincipal().getName()));
    }

    @DeleteMapping("/preliminaryRiskAssessments/delete/{id}")
    public ResponseEntity<PreliminaryRiskAssessment> delete(@PathVariable("id") UUID preliminaryRiskAssessmentId, HttpServletRequest request) {
        PreliminaryRiskAssessment preliminaryRiskAssessment = preliminaryRiskAssessmentService.delete(preliminaryRiskAssessmentId,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(preliminaryRiskAssessment);
    }
}
