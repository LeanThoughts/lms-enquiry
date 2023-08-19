package pfs.lms.enquiry.action.projectproposal.projectcost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectCostService implements IProjectCostService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final ProjectCostRepository projectCostRepository;

    @Override
    public ProjectCost create(ProjectCostResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        ProjectCost projectCost = new ProjectCost();
        projectCost.setProjectProposal(projectProposal);
        projectCost.setProjectCost(resource.getProjectCost());
        projectCost.setDebt(resource.getDebt());
        projectCost.setEquity(resource.getEquity());
        projectCost.setPfsDebtAmount(resource.getPfsDebtAmount());
        projectCost.setDebtEquityRatio(resource.getDebtEquityRatio());
        projectCost = projectCostRepository.save(projectCost);

        // Change Documents for Project Cost
        changeDocumentService.createChangeDocument(
                projectCost.getProjectProposal().getId(),
                projectCost.getId().toString(),
                projectCost.getProjectProposal().getId().toString(),
                projectCost.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                projectCost,
                "Created",
                username,
                "EnquiryAction", "Project Cost" );

        return projectCost;
    }

    @Override
    public ProjectCost update(ProjectCostResource resource, String username)
            throws CloneNotSupportedException {

        ProjectCost projectCost =
                projectCostRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

         Object oldObject = projectCost.clone();

        projectCost.setProjectCost(resource.getProjectCost());
        projectCost.setDebt(resource.getDebt());
        projectCost.setEquity(resource.getEquity());
        projectCost.setPfsDebtAmount(resource.getPfsDebtAmount());
        projectCost.setDebtEquityRatio(resource.getDebtEquityRatio());
        projectCost = projectCostRepository.save(projectCost);

        // Change Documents for Project Cost
        changeDocumentService.createChangeDocument(
                projectCost.getProjectProposal().getId(),
                projectCost.getId().toString(),
                projectCost.getProjectProposal().getId().toString(),
                projectCost.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                oldObject,
                projectCost,
                "Updated",
                username,
                "EnquiryAction", "Project Cost" );

        return projectCost;
    }
}
