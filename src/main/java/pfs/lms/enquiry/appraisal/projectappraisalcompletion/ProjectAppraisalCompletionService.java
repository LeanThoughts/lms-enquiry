package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAppraisalCompletionService implements IProjectAppraisalCompletionService {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProjectAppraisalCompletionRepository projectAppraisalCompletionRepository;

    @Override
    public ProjectAppraisalCompletion createProjectAppraisalCompletion(ProjectAppraisalCompletionResource projectAppraisalCompletionResource) {
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
        return projectAppraisalCompletion;
    }

    @Override
    public ProjectAppraisalCompletion updateProjectAppraisalCompletion(ProjectAppraisalCompletionResource projectAppraisalCompletionResource) {
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
        return projectAppraisalCompletion;
    }
}
