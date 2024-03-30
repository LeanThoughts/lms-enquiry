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
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectAppraisalCompletionService implements IProjectAppraisalCompletionService {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final ProjectAppraisalCompletionRepository projectAppraisalCompletionRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ProjectAppraisalCompletion createProjectAppraisalCompletion(ProjectAppraisalCompletionResource resource,   String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
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
        projectAppraisalCompletion.setAgendaNoteApprovalByDirA(resource.getAgendaNoteApprovalByDirA());
        projectAppraisalCompletion.setAgendaNoteApprovalByDirB(resource.getAgendaNoteApprovalByDirB());
        projectAppraisalCompletion.setAgendaNoteApprovalByMDAndCEO(resource.getAgendaNoteApprovalByMDAndCEO());
        projectAppraisalCompletion.setDirectorA(resource.getDirectorA());
        projectAppraisalCompletion.setDirectorB(resource.getDirectorB());
        projectAppraisalCompletion.setMdAndCEO(resource.getMdAndCEO());
        projectAppraisalCompletion.setDateOfProjectAppraisalCompletion(resource.
                getDateOfProjectAppraisalCompletion());
        projectAppraisalCompletion.setRemarks(resource.getRemarks());
        projectAppraisalCompletion = projectAppraisalCompletionRepository.save(projectAppraisalCompletion);
        projectAppraisalCompletion.setFileReference(resource.getFileReference());
        projectAppraisalCompletion.setDocumentTitle(resource.getDocumentTitle());
        projectAppraisalCompletion.setDocumentType(resource.getDocumentType());
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
    public ProjectAppraisalCompletion updateProjectAppraisalCompletion(ProjectAppraisalCompletionResource resource, String username) throws CloneNotSupportedException {
        ProjectAppraisalCompletion projectAppraisalCompletion =
                projectAppraisalCompletionRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldprojectAppraisalCompletion =  projectAppraisalCompletion.clone();

        projectAppraisalCompletion.setAgendaNoteApprovalByDirA(resource.
                getAgendaNoteApprovalByDirA());
        projectAppraisalCompletion.setAgendaNoteApprovalByDirB(resource.
                getAgendaNoteApprovalByDirB());
        projectAppraisalCompletion.setAgendaNoteApprovalByMDAndCEO(resource.
                getAgendaNoteApprovalByMDAndCEO());

        projectAppraisalCompletion.setDirectorA(resource.getDirectorA());
        projectAppraisalCompletion.setDirectorB(resource.getDirectorB());
        projectAppraisalCompletion.setMdAndCEO(resource.getMdAndCEO());

        projectAppraisalCompletion.setDateOfProjectAppraisalCompletion(resource.
                getDateOfProjectAppraisalCompletion());
        projectAppraisalCompletion.setRemarks(resource.getRemarks());

        if (resource.getFileReference() != null) {
            if (!resource.getFileReference().equals(""))
                projectAppraisalCompletion.setFileReference(resource.getFileReference());
        }

        // projectAppraisalCompletion.setFileReference(projectAppraisalCompletionResource.getFileReference());
        projectAppraisalCompletion.setDocumentTitle(resource.getDocumentTitle());
        projectAppraisalCompletion.setDocumentType(resource.getDocumentType());
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
