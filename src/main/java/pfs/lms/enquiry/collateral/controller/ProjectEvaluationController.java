package pfs.lms.enquiry.collateral.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.collateral.domain.ProjectEvaluation;
import pfs.lms.enquiry.collateral.dto.MajorCollateralTypeRequest;
import pfs.lms.enquiry.collateral.dto.ProjectEvaluationDto;
import pfs.lms.enquiry.collateral.service.ICollateralAuthorizationService;
import pfs.lms.enquiry.collateral.service.impl.ProjectEvaluationService;
import pfs.lms.enquiry.config.ApiController;
import pfs.lms.enquiry.exception.LmsException;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * REST API of the Project(Coll.) Valuation screen ("Maintain Project Valuation" on the collateral list). Paths sit
 * under /api. Every signed-in user may read; New, Change and Delete Valuation and the Major Collateral Type need the
 * collateral write role, like the collaterals themselves.
 */
@ApiController
public class ProjectEvaluationController {

    private final ProjectEvaluationService evaluationService;
    private final ICollateralAuthorizationService authorizationService;

    public ProjectEvaluationController(ProjectEvaluationService evaluationService,
                                       ICollateralAuthorizationService authorizationService) {
        this.evaluationService = evaluationService;
        this.authorizationService = authorizationService;
    }

    /** The screen: loan, Major Collateral Type, evaluations and whether they may be changed. */
    @GetMapping("/collaterals/loans/{loanApplicationId}/project-evaluations")
    public ResponseEntity<ProjectEvaluationDto> get(@PathVariable UUID loanApplicationId, HttpServletRequest request) {
        signedIn(request);
        return ResponseEntity.ok(evaluationService.get(loanApplicationId));
    }

    /** New Valuation */
    @PostMapping("/collaterals/loans/{loanApplicationId}/project-evaluations")
    public ResponseEntity<ProjectEvaluation> create(@PathVariable UUID loanApplicationId,
                                                    @RequestBody ProjectEvaluation evaluation,
                                                    HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.create(loanApplicationId, evaluation, user));
    }

    /** Change Valuation */
    @PutMapping("/collaterals/project-evaluations/{evaluationId}")
    public ResponseEntity<ProjectEvaluation> update(@PathVariable UUID evaluationId,
                                                    @RequestBody ProjectEvaluation evaluation,
                                                    HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.ok(evaluationService.update(evaluationId, evaluation, user));
    }

    /** Delete Valuation */
    @DeleteMapping("/collaterals/project-evaluations/{evaluationId}")
    public ResponseEntity<Void> delete(@PathVariable UUID evaluationId, HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        evaluationService.delete(evaluationId, user);
        return ResponseEntity.noContent().build();
    }

    /** Major Collateral Type of the loan: { "majorCollateralType": "2" }, blank to clear. */
    @PutMapping("/collaterals/loans/{loanApplicationId}/project-evaluations/major-collateral-type")
    public ResponseEntity<ProjectEvaluationDto> setMajorCollateralType(@PathVariable UUID loanApplicationId,
                                                                       @RequestBody MajorCollateralTypeRequest body,
                                                                       HttpServletRequest request) {
        String user = currentUser(request);
        authorizationService.checkWriteAccess(user);
        return ResponseEntity.ok(evaluationService.setMajorCollateralType(loanApplicationId,
                body == null ? null : body.getMajorCollateralType(), user));
    }

    private static void signedIn(HttpServletRequest request) {
        if (currentUser(request) == null) {
            throw new LmsException("Please sign in.", HttpStatus.UNAUTHORIZED);
        }
    }

    private static String currentUser(HttpServletRequest request) {
        return request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
    }
}
