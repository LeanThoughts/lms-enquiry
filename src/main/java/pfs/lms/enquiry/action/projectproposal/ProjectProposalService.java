package pfs.lms.enquiry.action.projectproposal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletion;
import pfs.lms.enquiry.action.enquirycompletion.EnquiryCompletionRepository;
import pfs.lms.enquiry.action.otherdetail.OtherDetailRepository;
import pfs.lms.enquiry.action.projectproposal.dealguaranteetimeline.DealGuaranteeTimelineRepository;
import pfs.lms.enquiry.action.projectproposal.projectcost.ProjectCost;
import pfs.lms.enquiry.action.projectproposal.projectcost.ProjectCostRepository;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetail;
import pfs.lms.enquiry.action.projectproposal.projectdetail.ProjectDetailRepository;
import pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail.ProjectProposalOtherDetail;
import pfs.lms.enquiry.action.projectproposal.projectproposalotherdetail.ProjectProposalOtherDetailRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.domain.Partner;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.repository.PartnerRepository;
import pfs.lms.enquiry.repository.UserRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectProposalService implements IProjectProposalService {
    private final ProjectProposalOtherDetailRepository projectProposalOtherDetailRepository;
    private final OtherDetailRepository otherDetailRepository;
    private final ProjectCostRepository projectCostRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final EnquiryCompletionRepository enquiryCompletionRepository;

    private final IChangeDocumentService changeDocumentService;

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final ProjectProposalRepository projectProposalRepository;
    private final DealGuaranteeTimelineRepository dealGuaranteeTimelineRepository;
    private final UserRepository userRepository;

    private final PartnerRepository partnerRepository;

//    @Override
//    public ProjectProposalResource getProjectProposal(UUID enquiryActionId) {
//        ProjectProposal projectProposal = projectProposalRepository.findByEnquiryActionId(enquiryActionId)
//                .orElseGet(() -> {
//                    return null;
//                });
//        ProjectProposalResource projectProposalResource = null;
//        if (projectProposal != null) {
//            projectProposalResource = new ProjectProposalResource();
//            projectProposalResource.setId(projectProposal.getId());
//            DealGuaranteeTimeline dealGuaranteeTimeline = dealGuaranteeTimelineRepository.
//                    findByProjectProposalId(projectProposal.getId());
//            projectProposalResource.setDealGuaranteeTimeline(dealGuaranteeTimeline);
//        }
//        return projectProposalResource;
//    }

    @Override
    public ProjectProposal create(ProjectProposalResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        EnquiryAction enquiryAction = enquiryActionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    EnquiryAction obj = new EnquiryAction();
                    obj.setLoanApplication(loanApplication);
                    obj = enquiryActionRepository.save(obj);
                    // Change Documents for EnquiryAction Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "EnquiryAction", "Header");
                    return obj;
                });
        ProjectProposal projectProposal = new ProjectProposal();
        projectProposal.setEnquiryAction(enquiryAction);
        projectProposal.setSerialNumber(projectProposalRepository.
                findByEnquiryActionId(enquiryAction.getId()).size() + 1);
        projectProposal.setAdditionalDetails(resource.getAdditionalDetails());
        projectProposal.setProposalFormSharingDate(resource.getProposalFormSharingDate());
        projectProposal.setDocumentName(resource.getDocumentName());
        projectProposal.setDocumentType(resource.getDocumentType());
        projectProposal.setDocumentVersion(resource.getDocumentVersion());
        projectProposal.setProposalStatus(resource.getProposalStatus());
        projectProposal.setLastChangedByUser(username);
        projectProposal = projectProposalRepository.save(projectProposal);


        // Change Documents for Project Proposal
        changeDocumentService.createChangeDocument(
                projectProposal.getEnquiryAction().getId(),
                projectProposal.getId().toString(),
                projectProposal.getEnquiryAction().getId().toString(),
                projectProposal.getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                projectProposal,
                "Created",
                username,
                "EnquiryAction", "Project Proposal" );

        return projectProposal;
    }

    @Override
    public ProjectProposal update(ProjectProposalResource resource, String username)
            throws CloneNotSupportedException {
        ProjectProposal projectProposal =
                projectProposalRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldRejectByPFS = projectProposal.clone();

        projectProposal.setAdditionalDetails(resource.getAdditionalDetails());
        projectProposal.setProposalFormSharingDate(resource.getProposalFormSharingDate());
        projectProposal.setDocumentName(resource.getDocumentName());
        projectProposal.setDocumentType(resource.getDocumentType());
        projectProposal.setDocumentVersion(resource.getDocumentVersion());
        projectProposal.setProposalStatus(resource.getProposalStatus());
        projectProposal.setLastChangedByUser(username);
        projectProposal = projectProposalRepository.save(projectProposal);

        // Change Documents for Project Proposal
        changeDocumentService.createChangeDocument(
                projectProposal.getEnquiryAction().getId(),
                projectProposal.getId().toString(),
                projectProposal.getEnquiryAction().getId().toString(),
                projectProposal.getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                oldRejectByPFS,
                projectProposal,
                "Updated",
                username,
                "EnquiryAction", "Project Proposal" );

        return projectProposal;
    }

    @Override
    public ProjectProposal processApprovedEnquiry(EnquiryAction enquiryAction, String username) throws CloneNotSupportedException {

        //Get the Project Proposal with the Final Status

        List<ProjectProposal> projectProposalList = projectProposalRepository.findByEnquiryActionId(enquiryAction.getId());
        ProjectProposal projectProposalFinal = projectProposalList.stream()
                .filter(projectProposal -> "Final".equals(projectProposal.getProposalStatus()))
                .findFirst()
                .orElse(null);
        if (projectProposalFinal == null)
            return null;

        ProjectDetail projectDetail = projectDetailRepository.findByProjectProposalId(projectProposalFinal.getId());

        // Update Loan Application with Project Details
        this.updateLoanApplicationFromProposal(projectProposalFinal,   username);


        return projectProposalFinal;
    }


    private LoanApplication updateLoanApplicationFromProposal( ProjectProposal projectProposal, String username){

        loanApplicationRepository.flush();

        LoanApplication loanApplication = projectProposal.getEnquiryAction().getLoanApplication();

        ProjectDetail projectDetail = projectDetailRepository.findByProjectProposalId(projectProposal.getId());
        ProjectCost projectCost     = projectCostRepository.findByProjectProposalId(projectProposal.getId());
        ProjectProposalOtherDetail projectProposalOtherDetail     = projectProposalOtherDetailRepository.findByProjectProposalId(projectProposal.getId());
        EnquiryCompletion enquiryCompletion = enquiryCompletionRepository.findByEnquiryActionId(projectProposal.getEnquiryAction().getId());

        loanApplication.setProjectName(projectDetail.getProjectName());
        loanApplication.setPromoterName(projectDetail.getPromoterName());
        loanApplication.setProjectLocationState(projectDetail.getState());
        loanApplication.setProjectDistrict(projectDetail.getDistrict());

        if (enquiryCompletion!= null) {
            loanApplication.setProductCode(enquiryCompletion.getProductType());
            loanApplication.setTerm(enquiryCompletion.getTerm());
            loanApplication.setDecisionDate(enquiryCompletion.getDate());
            loanApplication.setEnquiryCompletionDate(enquiryCompletion.getDate());
            loanApplication.setEnquiryRemarks(enquiryCompletion.getRemarks());
        }

        loanApplication.setLoanType(projectDetail.getLoanType());
        loanApplication.setLoanClass(projectDetail.getLoanClass());
        loanApplication.setFinancingType(projectDetail.getFinancingType());
        loanApplication.setAssistanceType(projectDetail.getAssistanceType());
        loanApplication.setProjectType(projectDetail.getProjectType());
        loanApplication.setProjectCoreSector(projectDetail.getProjectTypeCoreSector());
        loanApplication.setLoanPurpose(projectDetail.getLoanPurpose());
        loanApplication.setPurposeOfLoan(projectDetail.getPurposeOfLoan());

        loanApplication.setProjectCoreSector(projectDetail.getProjectCoreSector());
        loanApplication.setRenewableFlag(projectDetail.getRenewableFlag());
        loanApplication.setPolicyExposure(projectDetail.getPolicyExposure());

        loanApplication.setProjectCapacity(projectDetail.getProjectCapacity());
        loanApplication.setProjectCapacityUnit(projectDetail.getProjectCapacityUnit());

        loanApplication.setEndUseOfFunds(projectDetail.getEndUseOfFunds());
        loanApplication.setFees(projectDetail.getFees());
        loanApplication.setExpectedInterestRate(projectDetail.getRoi());
        loanApplication.setTenorYear(projectDetail.getTenorYear());
        loanApplication.setTenorMonth(projectDetail.getTenorMonths());
        loanApplication.setMoratoriumPeriod(projectDetail.getMoratoriumPeriod());
        loanApplication.setMoratoriumPeriodUnit(projectDetail.getMoratoriumPeriodUnit());
        loanApplication.setConstructionPeriod(projectDetail.getConstructionPeriod());
        loanApplication.setConstructionPeriodUnit(projectDetail.getConstructionPeriodUnit());

        if (projectCost != null) {
            loanApplication.setProjectCost(projectCost.getProjectCost());
            loanApplication.setProjectDebtAmount(projectCost.getDebt());
            loanApplication.setEquity(projectCost.getEquity());
            loanApplication.setPfsDebtAmount(projectCost.getPfsDebtAmount());
        }

        if (projectProposalOtherDetail != null){
            loanApplication.setSourceAndCashFlow(projectProposalOtherDetail.getSourceAndCashFlow());
            loanApplication.setOptimumDateOfLoan(projectProposalOtherDetail.getOptimumDateOfLoan());
            loanApplication.setConsolidatedGroupLeverage(projectProposalOtherDetail.getConsolidatedGroupLeverage());
            loanApplication.setTotalDebtTNW(projectProposalOtherDetail.getTotalDebtTNW());
            loanApplication.setTolTNW(projectProposalOtherDetail.getTolTNW());
            loanApplication.setTotalDebtTNWPercentage(projectProposalOtherDetail.getTotalDebtTNWPercentage());
            loanApplication.setTolTNWPercentage(projectProposalOtherDetail.getTolTNWPercentage());
            loanApplication.setDelayInDebtServicing(projectProposalOtherDetail.getDelayInDebtServicing());
        }

        loanApplication.getbusPartnerNumber();
        Partner partner = partnerRepository.findByPartyNumber(Integer.parseInt(loanApplication.getbusPartnerNumber()));
        partner.setPartyName1(projectDetail.getBorrowerName());

        loanApplication.setChangedByUserName(projectProposal.getLastChangedByUser());

        loanApplication.setFunctionalStatus(1); //Enquiry Stage
        loanApplication.setTechnicalStatus(3);  // Submitted
        loanApplication.setPostedInSAP(0);
        loanApplication.setFinalDecisionStatus(0); //

        loanApplicationRepository.save(loanApplication);
        partnerRepository.save(partner);

        return  null;
    }
}
