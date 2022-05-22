package pfs.lms.enquiry.appraisal.projectappraisalcompletion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAppraisalCompletionService implements IProjectAppraisalCompletionService {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProjectAppraisalCompletionRepository projectAppraisalCompletionRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ProjectAppraisalCompletion createProjectAppraisalCompletion(ProjectAppraisalCompletionResource projectAppraisalCompletionResource,   String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(projectAppraisalCompletionResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),null,
                            loanApplication.getLoanContractId(),
                            obj.getId().toString(),
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");

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
        // Change Documents for project Appraisal Completion
        changeDocumentService.createChangeDocument(
                projectAppraisalCompletion.getLoanAppraisal().getId(),
                projectAppraisalCompletion.getId().toString(),
                projectAppraisalCompletion.getLoanAppraisal().getId().toString(),
                projectAppraisalCompletion.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                projectAppraisalCompletion,
                "Created",
                username,
                "Appraisal", "Project Appraisal Completion" );
        return projectAppraisalCompletion;
    }

    @Override
    public ProjectAppraisalCompletion updateProjectAppraisalCompletion(ProjectAppraisalCompletionResource projectAppraisalCompletionResource, String username) throws CloneNotSupportedException {
        ProjectAppraisalCompletion projectAppraisalCompletion =
                projectAppraisalCompletionRepository.findById(projectAppraisalCompletionResource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(projectAppraisalCompletionResource.getId().toString()));

        Object oldprojectAppraisalCompletion =  projectAppraisalCompletion.clone();

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



        // Change Documents for project Appraisal Completion
        changeDocumentService.createChangeDocument(
                projectAppraisalCompletion.getLoanAppraisal().getId(),
                projectAppraisalCompletion.getId().toString(),
                projectAppraisalCompletion.getLoanAppraisal().getId().toString(),
                projectAppraisalCompletion.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                oldprojectAppraisalCompletion,
                projectAppraisalCompletion,
                "Updated",
                username,
                "Appraisal", "Project Appraisal Completion" );

        return projectAppraisalCompletion;
    }
}
