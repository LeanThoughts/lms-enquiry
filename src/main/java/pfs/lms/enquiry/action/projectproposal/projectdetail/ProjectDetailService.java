package pfs.lms.enquiry.action.projectproposal.projectdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.ProjectProposal;
import pfs.lms.enquiry.action.projectproposal.ProjectProposalRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDetailService implements IProjectDetailService {
    private final LoanApplicationRepository loanApplicationRepository;

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final ProjectDetailRepository projectDetailRepository;

    @Override
    public ProjectDetail create(ProjectDetailResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        ProjectDetail projectDetail = new ProjectDetail();
        projectDetail.setProjectProposal(projectProposal);
        projectDetail.setProjectName(resource.getProjectName());
        projectDetail.setBorrowerName(resource.getBorrowerName());
        projectDetail.setPromoterName(resource.getPromoterName());
        projectDetail.setLoanPurpose(resource.getLoanPurpose());
        projectDetail.setProjectCapacity(resource.getProjectCapacity());
        projectDetail.setProjectCapacityUnit(resource.getProjectCapacityUnit());
        projectDetail.setState(resource.getState());
        projectDetail.setDistrict(resource.getDistrict());

        projectDetail.setProjectType(resource.getProjectType());
        projectDetail.setLoanType(resource.getLoanType());
        projectDetail.setProjectTypeCoreSector(resource.getProjectTypeCoreSector());
        projectDetail.setPurposeOfLoan(resource.getPurposeOfLoan());

        projectDetail.setLoanClass(resource.getLoanClass());
        projectDetail.setAssistanceType(resource.getAssistanceType());
        projectDetail.setFinancingType(resource.getFinancingType());
        projectDetail.setProjectType(resource.getProjectType());
        projectDetail.setProjectCoreSector(resource.getProjectCoreSector());
        projectDetail.setRenewableFlag(resource.getRenewableFlag());
        projectDetail.setPolicyExposure(resource.getPolicyExposure());
        projectDetail.setEndUseOfFunds(resource.getEndUseOfFunds());
        projectDetail.setFees(resource.getFees());
        projectDetail.setTenorYear(resource.getTenorYear());
        projectDetail.setTenorMonths(resource.getTenorMonths());
        projectDetail.setMoratoriumPeriod(resource.getMoratoriumPeriod());
        projectDetail.setMoratoriumPeriodUnit(resource.getMoratoriumPeriodUnit());
        projectDetail.setConstructionPeriod(resource.getConstructionPeriod());
        projectDetail.setConstructionPeriodUnit(resource.getConstructionPeriodUnit());
        projectDetail.setStatus(resource.getStatus());
        projectDetail.setRoi(resource.getRoi());
        projectDetail = projectDetailRepository.save(projectDetail);

        // Change Documents for Project Detail
        changeDocumentService.createChangeDocument(
                projectDetail.getProjectProposal().getId(),
                projectDetail.getId().toString(),
                projectDetail.getProjectProposal().getId().toString(),
                projectDetail.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                null,
                projectDetail,
                "Created",
                username,
                "EnquiryAction", "Project Detail" );

        return projectDetail;
    }

    @Override
    public ProjectDetail update(ProjectDetailResource resource, String username)
            throws CloneNotSupportedException {

        ProjectDetail projectDetail =
                projectDetailRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

         Object oldObject = projectDetail.clone();

        projectDetail.setProjectName(resource.getProjectName());
        projectDetail.setBorrowerName(resource.getBorrowerName());
        projectDetail.setPromoterName(resource.getPromoterName());
        projectDetail.setLoanPurpose(resource.getLoanPurpose());
        projectDetail.setProjectCapacity(resource.getProjectCapacity());
        projectDetail.setProjectCapacityUnit(resource.getProjectCapacityUnit());
        projectDetail.setState(resource.getState());
        projectDetail.setDistrict(resource.getDistrict());
        projectDetail.setLoanClass(resource.getLoanClass());
        projectDetail.setAssistanceType(resource.getAssistanceType());
        projectDetail.setFinancingType(resource.getFinancingType());
        projectDetail.setProjectType(resource.getProjectType());
        projectDetail.setProjectCoreSector(resource.getProjectCoreSector());
        projectDetail.setRenewableFlag(resource.getRenewableFlag());
        projectDetail.setPolicyExposure(resource.getPolicyExposure());
        projectDetail.setEndUseOfFunds(resource.getEndUseOfFunds());
        projectDetail.setFees(resource.getFees());
        projectDetail.setTenorYear(resource.getTenorYear());
        projectDetail.setTenorMonths(resource.getTenorMonths());
        projectDetail.setMoratoriumPeriod(resource.getMoratoriumPeriod());
        projectDetail.setMoratoriumPeriodUnit(resource.getMoratoriumPeriodUnit());
        projectDetail.setConstructionPeriod(resource.getConstructionPeriod());
        projectDetail.setConstructionPeriodUnit(resource.getConstructionPeriodUnit());
        projectDetail.setStatus(resource.getStatus());
        projectDetail.setRoi(resource.getRoi());

        projectDetail.setProjectType(resource.getProjectType());
        projectDetail.setLoanType(resource.getLoanType());
        projectDetail.setProjectTypeCoreSector(resource.getProjectTypeCoreSector());
        projectDetail.setPurposeOfLoan(resource.getPurposeOfLoan());

        projectDetail = projectDetailRepository.save(projectDetail);

        // Change Documents for Project Detail
        changeDocumentService.createChangeDocument(
                projectDetail.getProjectProposal().getId(),
                projectDetail.getId().toString(),
                projectDetail.getProjectProposal().getId().toString(),
                projectDetail.getProjectProposal().getEnquiryAction().getLoanApplication().getEnquiryNo().getId().toString(),
                oldObject,
                projectDetail,
                "Updated",
                username,
                "EnquiryAction", "Project Detail" );

        //updateLoanApplication(projectDetail, projectDetail.getProjectProposal());

        return projectDetail;
    }

    LoanApplication updateLoanApplication(ProjectDetail projectDetail, ProjectProposal projectProposal){

        LoanApplication loanApplication = projectProposal.getEnquiryAction().getLoanApplication();

        loanApplication.setProjectName(projectDetail.getProjectName());
        loanApplication.setPromoterName(projectDetail.getPromoterName());
        loanApplication.setLoanPurpose(projectDetail.getLoanPurpose());
        loanApplication.setEndUseOfFunds(projectDetail.getEndUseOfFunds());
        loanApplication.setProjectCapacity(projectDetail.getProjectCapacity());
        loanApplication.setProjectCapacityUnit(projectDetail.getProjectCapacityUnit());
        loanApplication.setProjectLocationState(projectDetail.getState());
        loanApplication.setProjectDistrict(projectDetail.getDistrict());
        loanApplication.setLoanClass(projectDetail.getLoanClass());
        loanApplication.setAssistanceType(projectDetail.getAssistanceType());
        loanApplication.setFinancingType(projectDetail.getFinancingType());
        loanApplication.setProjectType(projectDetail.getProjectType());
        loanApplication.setProjectCoreSector(projectDetail.getProjectCoreSector());
        loanApplication.setRenewableFlag(projectDetail.getRenewableFlag());
        loanApplication.setPolicyExposure(projectDetail.getPolicyExposure());
        loanApplication.setFees(projectDetail.getFees());
        loanApplication.setTenorYear(projectDetail.getTenorYear());
        loanApplication.setTenorMonth(projectDetail.getTenorMonths());
        loanApplication.setMoratoriumPeriodUnit(projectDetail.getMoratoriumPeriodUnit());
        loanApplication.setMoratoriumPeriod(projectDetail.getMoratoriumPeriod());
        loanApplication.setConstructionPeriodUnit(projectDetail.getConstructionPeriodUnit());
        loanApplication.setConstructionPeriod(projectDetail.getConstructionPeriod());
        loanApplication.setExpectedInterestRate(projectDetail.getRoi());
        loanApplication.setProjectType(projectDetail.getProjectType());
        loanApplication.setLoanType(projectDetail.getLoanType());
        loanApplication.setProjectTypeCoreSector(projectDetail.getProjectTypeCoreSector());
        loanApplication.setPurposeOfLoan(projectDetail.getPurposeOfLoan());

        loanApplicationRepository.save(loanApplication);
        return loanApplication;
    }
}
