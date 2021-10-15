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
    public ProjectDataResource getProjectData(UUID loanAppraisalId) {
        ProjectData projectData = projectDataRepository.findByLoanAppraisalId((loanAppraisalId));
        ProjectDataResource projectDataResource = null;
        if (projectData != null) {
            projectDataResource = new ProjectDataResource();
            BeanUtils.copyProperties(projectData, projectDataResource);
        }
        return projectDataResource;
    }

    @Override
    public ProjectDataResource createProjectData(ProjectDataResource projectDataResource) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(projectDataResource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    LoanAppraisal obj = new LoanAppraisal();
                    obj.setLoanApplication(loanApplication);
                    obj = loanAppraisalRepository.save(obj);
                    return obj;
                });
        ProjectData projectData = new ProjectData();
        projectData.setLoanAppraisal(loanAppraisal);
        projectData.setEpcContractor(projectDataResource.getEpcContractor());
        projectData.setMainContractor(projectDataResource.getMainContractor());
        projectData.setNumberOfUnits(projectDataResource.getNumberOfUnits());
        projectData.setPolicyApplicable(projectDataResource.getPolicyApplicable());
        projectData.setProjectCapacity(projectDataResource.getProjectCapacity());
        projectData.setProjectCapacityUnit(projectDataResource.getProjectCapacityUnit());
        projectData.setProjectName(projectDataResource.getProjectName());
        projectData.setProjectType(projectDataResource.getProjectType());
        projectData.setResourceAssemblyAgency(projectDataResource.getResourceAssemblyAgency());
        projectData.setTypeOfFunding(projectDataResource.getTypeOfFunding());
        projectData = projectDataRepository.save(projectData);
        BeanUtils.copyProperties(projectData, projectDataResource);
        return projectDataResource;
    }

    @Override
    public ProjectDataResource updateProjectData(ProjectDataResource projectDataResource) {
        ProjectData projectData = projectDataRepository.findById(projectDataResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(projectDataResource.getId().toString()));
        projectData.setEpcContractor(projectDataResource.getEpcContractor());
        projectData.setMainContractor(projectDataResource.getMainContractor());
        projectData.setNumberOfUnits(projectDataResource.getNumberOfUnits());
        projectData.setPolicyApplicable(projectDataResource.getPolicyApplicable());
        projectData.setProjectCapacity(projectDataResource.getProjectCapacity());
        projectData.setProjectCapacityUnit(projectDataResource.getProjectCapacityUnit());
        projectData.setProjectName(projectDataResource.getProjectName());
        projectData.setProjectType(projectDataResource.getProjectType());
        projectData.setResourceAssemblyAgency(projectDataResource.getResourceAssemblyAgency());
        projectData.setTypeOfFunding(projectDataResource.getTypeOfFunding());
        projectData = projectDataRepository.save(projectData);
        BeanUtils.copyProperties(projectData, projectDataResource);
        return projectDataResource;
    }
}
