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

        // Change Documents for Project Proposal
//        changeDocumentService.createChangeDocument(
//                projectProposal.getEnquiryAction().getId(),
//                projectProposal.getId().toString(),
//                projectProposal.getEnquiryAction().getId().toString(),
//                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                null,
//                projectProposal,
//                "Created",
//                username,
//                "EnquiryAction", "Project Proposal" );

        return projectCost;
    }

    @Override
    public ProjectCost update(ProjectCostResource resource, String username)
            throws CloneNotSupportedException {

        ProjectCost projectCost =
                projectCostRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        // Object oldRejectByPFS = projectProposal.clone();

        projectCost.setProjectCost(resource.getProjectCost());
        projectCost.setDebt(resource.getDebt());
        projectCost.setEquity(resource.getEquity());
        projectCost.setPfsDebtAmount(resource.getPfsDebtAmount());
        projectCost.setDebtEquityRatio(resource.getDebtEquityRatio());
        projectCost = projectCostRepository.save(projectCost);

        // Change Documents for Project Proposal
//        changeDocumentService.createChangeDocument(
//                projectProposal.getEnquiryAction().getId(),
//                projectProposal.getId().toString(),
//                projectProposal.getEnquiryAction().getId().toString(),
//                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
//                oldRejectByPFS,
//                projectProposal,
//                "Updated",
//                username,
//                "EnquiryAction", "Project Proposal" );

        return projectCost;
    }
}
