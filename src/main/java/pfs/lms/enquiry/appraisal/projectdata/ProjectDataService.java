package pfs.lms.enquiry.appraisal.projectdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDataService implements IProjectDataService {

    private final ProjectDataRepository projectDataRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;

    @Override
    public ProjectData getProjectData(UUID loanAppraisalId) {
        ProjectData projectData = projectDataRepository.findByLoanAppraisalId((loanAppraisalId));
        return projectData;
    }

    @Override
    public ProjectData createProjectData(ProjectDataResource projectDataResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(projectDataResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        ProjectData projectData = new ProjectData();
        BeanUtils.copyProperties(projectDataResource, projectData);
        projectData.setLoanAppraisal(loanAppraisal);
        projectData = projectDataRepository.save(projectData);
        return projectData;
    }

    @Override
    public ProjectData updateProjectData(ProjectDataResource projectDataResource) {
        ProjectData projectData = projectDataRepository.findById(projectDataResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(projectDataResource.getId().toString()));
        BeanUtils.copyProperties(projectDataResource, projectData);
        projectData = projectDataRepository.save(projectData);
        return projectData;
    }
}
