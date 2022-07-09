package pfs.lms.enquiry.appraisal.projectdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.riskreport.IRiskClient;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableFeignClients
public class ProjectDataService implements IProjectDataService {

    private final ProjectDataRepository projectDataRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalRepository loanAppraisalRepository;
    private final IChangeDocumentService changeDocumentService;

    private final IRiskClient riskClient;

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
    public ProjectData updateProjectData(ProjectDataResource projectDataResource, String username, HttpServletRequest request)
            throws CloneNotSupportedException {

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

//        List<RiskEvaluationSummary> resources =
//            riskClient.findRiskModelSummaryForLoanContractId("0000020000610",
//                        getAuthorizationBearer(request.getUserPrincipal()));

        //RestTemplate restTemplate = new RestTemplate();
        //String resourceUrl = "http://localhost:8090/risk/api/riskEvaluationSummary/loanContractId/0000020000543";
        //<String> response = restTemplate.getForEntity(resourceUrl, String.class);

        return projectData;
    }

    public String getAuthorizationBearer(Principal user) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) ((OAuth2Authentication) user).getDetails();
        return "Bearer " + details.getTokenValue();
    }

}
