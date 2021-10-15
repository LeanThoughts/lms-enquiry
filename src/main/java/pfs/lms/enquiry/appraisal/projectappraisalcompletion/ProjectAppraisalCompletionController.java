package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
public class ProjectAppraisalCompletionController {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProjectAppraisalCompletionRepository projectAppraisalCompletionRepository;

    @PostMapping("/projectAppraisalCompletions/create")
    public ResponseEntity<ProjectAppraisalCompletion> createProjectAppraisalCompletion(
                @RequestBody ProjectAppraisalCompletionResource projectAppraisalCompletionResource,
                HttpServletRequest request) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(projectAppraisalCompletionResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        ProjectAppraisalCompletion projectAppraisalCompletion = new ProjectAppraisalCompletion();
        projectAppraisalCompletion.setLoanAppraisal(loanAppraisal);
        projectAppraisalCompletion.setAgendaNoteApprovalByDirA(projectAppraisalCompletionResource.
                getAgendaNoteApprovalByDirA());
        projectAppraisalCompletion.setAgendaNoteApprovalByDirB(projectAppraisalCompletionResource.
                getAgendaNoteApprovalByDirB());
        projectAppraisalCompletion.setAgendaNoteApprovalByMDAndCEO(projectAppraisalCompletionResource.
                getAgendaNoteApprovalByMDAndCEO());
        projectAppraisalCompletion.setAgendaNoteSubmissionToCoSecy(projectAppraisalCompletionResource.
                getAgendaNoteSubmissionToCoSecy());
        projectAppraisalCompletion.setDateOfProjectAppraisalCompletion(projectAppraisalCompletionResource.
                getDateOfProjectAppraisalCompletion());
        projectAppraisalCompletion.setRemarks(projectAppraisalCompletionResource.getRemarks());
        projectAppraisalCompletion = projectAppraisalCompletionRepository.save(projectAppraisalCompletion);
        return ResponseEntity.ok(projectAppraisalCompletion);
    }

    @PutMapping("/projectAppraisalCompletions/update")
    public ResponseEntity<ProjectAppraisalCompletion> updateProjectAppraisalCompletion(
                @RequestBody ProjectAppraisalCompletionResource projectAppraisalCompletionResource,
                HttpServletRequest request) {

        ProjectAppraisalCompletion projectAppraisalCompletion =
                projectAppraisalCompletionRepository.findById(projectAppraisalCompletionResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(projectAppraisalCompletionResource.getId().toString()));
        projectAppraisalCompletion.setAgendaNoteApprovalByDirA(projectAppraisalCompletionResource.
                getAgendaNoteApprovalByDirA());
        projectAppraisalCompletion.setAgendaNoteApprovalByDirB(projectAppraisalCompletionResource.
                getAgendaNoteApprovalByDirB());
        projectAppraisalCompletion.setAgendaNoteApprovalByMDAndCEO(projectAppraisalCompletionResource.
                getAgendaNoteApprovalByMDAndCEO());
        projectAppraisalCompletion.setAgendaNoteSubmissionToCoSecy(projectAppraisalCompletionResource.
                getAgendaNoteSubmissionToCoSecy());
        projectAppraisalCompletion.setDateOfProjectAppraisalCompletion(projectAppraisalCompletionResource.
                getDateOfProjectAppraisalCompletion());
        projectAppraisalCompletion.setRemarks(projectAppraisalCompletionResource.getRemarks());
        projectAppraisalCompletion = projectAppraisalCompletionRepository.save(projectAppraisalCompletion);
        return ResponseEntity.ok(projectAppraisalCompletion);
    }
}
