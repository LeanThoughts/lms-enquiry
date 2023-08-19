package pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail;

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
public class ProjectProposalOtherDetailService implements IProjectProposalOtherDetailService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final ProjectProposalOtherDetailRepository projectProposalOtherDetailRepository;

    @Override
    public ProjectProposalOtherDetail create(ProjectProposalOtherDetailResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        ProjectProposalOtherDetail projectProposalOtherDetail = new ProjectProposalOtherDetail();
        projectProposalOtherDetail.setProjectProposal(projectProposal);
        projectProposalOtherDetail.setSourceAndCashFlow(resource.getSourceAndCashFlow());
        projectProposalOtherDetail.setOptimumDateOfLoan(resource.getOptimumDateOfLoan());
        projectProposalOtherDetail.setConsolidatedGroupLeverage(resource.getConsolidatedGroupLeverage());
        projectProposalOtherDetail.setTotalDebtTNW(resource.getTotalDebtTNW());
        projectProposalOtherDetail.setTolTNW(resource.getTolTNW());
        projectProposalOtherDetail.setTotalDebtTNWPercentage(resource.getTotalDebtTNWPercentage());
        projectProposalOtherDetail.setTolTNWPercentage(resource.getTolTNWPercentage());
        projectProposalOtherDetail.setDelayInDebtServicing(resource.getDelayInDebtServicing());
        projectProposalOtherDetail = projectProposalOtherDetailRepository.save(projectProposalOtherDetail);

        // Change Documents for Project Proposal Other Detail
        changeDocumentService.createChangeDocument(
                projectProposalOtherDetail.getProjectProposal().getId(),
                projectProposalOtherDetail.getId().toString(),
                projectProposalOtherDetail.getProjectProposal().getId().toString(),
                projectProposalOtherDetail.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                projectProposalOtherDetail,
                "Created",
                username,
                "EnquiryAction", "Project Proposal Other Detail" );

        return projectProposalOtherDetail;
    }

    @Override
    public ProjectProposalOtherDetail update(ProjectProposalOtherDetailResource resource, String username)
            throws CloneNotSupportedException {

        ProjectProposalOtherDetail projectProposalOtherDetail =
                projectProposalOtherDetailRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

          Object oldObject = projectProposalOtherDetail.clone();

        projectProposalOtherDetail.setSourceAndCashFlow(resource.getSourceAndCashFlow());
        projectProposalOtherDetail.setOptimumDateOfLoan(resource.getOptimumDateOfLoan());
        projectProposalOtherDetail.setConsolidatedGroupLeverage(resource.getConsolidatedGroupLeverage());
        projectProposalOtherDetail.setTotalDebtTNW(resource.getTotalDebtTNW());
        projectProposalOtherDetail.setTolTNW(resource.getTolTNW());
        projectProposalOtherDetail.setTotalDebtTNWPercentage(resource.getTotalDebtTNWPercentage());
        projectProposalOtherDetail.setTolTNWPercentage(resource.getTolTNWPercentage());
        projectProposalOtherDetail.setDelayInDebtServicing(resource.getDelayInDebtServicing());
        projectProposalOtherDetail = projectProposalOtherDetailRepository.save(projectProposalOtherDetail);

        // Change Documents for Project Proposal Other Detail
        changeDocumentService.createChangeDocument(
                projectProposalOtherDetail.getProjectProposal().getId(),
                projectProposalOtherDetail.getId().toString(),
                projectProposalOtherDetail.getProjectProposal().getId().toString(),
                projectProposalOtherDetail.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                oldObject,
                projectProposalOtherDetail,
                "Updated",
                username,
                "EnquiryAction", "Project Proposal Other Detail" );

        return projectProposalOtherDetail;
    }
}
