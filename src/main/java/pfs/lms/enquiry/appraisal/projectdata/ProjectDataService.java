package pfs.lms.enquiry.appraisal.projectdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDataService implements IProjectDataService {

    private final ProjectDataRepository projectDataRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public ProjectData getProjectData(UUID loanAppraisalId) {
        ProjectData projectData = projectDataRepository.findByLoanAppraisalId((loanAppraisalId));
        return projectData;
    }

    @Override
    public ProjectData createProjectData(ProjectDataResource projectDataResource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(projectDataResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);

                    // Change Documents for Appraisal Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),
                            obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Appraisal", "Header");

                    return obj;
                });
        ProjectData projectData = new ProjectData();
        BeanUtils.copyProperties(projectDataResource, projectData);
        projectData.setLoanAppraisal(loanAppraisal);
        projectData = projectDataRepository.save(projectData);

        // Change Documents for project Appraisal Completion
        changeDocumentService.createChangeDocument(
                projectData.getLoanAppraisal().getId(),
                projectData.getId().toString(),
                projectData.getLoanAppraisal().getId().toString(),
                projectData.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                null,
                projectData,
                "Created",
                username,
                "Appraisal", "Project Data" );

        return projectData;
    }

    @Override
    public ProjectData updateProjectData(ProjectDataResource projectDataResource, String username) throws CloneNotSupportedException {
        ProjectData projectData = projectDataRepository.findById(projectDataResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(projectDataResource.getId().toString()));
        BeanUtils.copyProperties(projectDataResource, projectData);

        Object odlProjectData = projectData.clone();

        projectData = projectDataRepository.save(projectData);
        // Change Documents for project Appraisal Completion
        changeDocumentService.createChangeDocument(
                projectData.getLoanAppraisal().getId(),
                projectData.getId().toString(),
                projectData.getLoanAppraisal().getId().toString(),
                projectData.getLoanAppraisal().getLoanApplication().getLoanContractId(),
                odlProjectData,
                projectData,
                "Updated",
                username,
                "Appraisal", "Project Data" );
        return projectData;
    }
}
