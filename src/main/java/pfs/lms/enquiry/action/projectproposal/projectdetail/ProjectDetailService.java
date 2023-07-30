package pfs.lms.enquiry.action.projectproposal.projectdetail;

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
public class ProjectDetailService implements IProjectDetailService {

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
        projectDetail.setProductType(resource.getProductType());
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

        return projectDetail;
    }

    @Override
    public ProjectDetail update(ProjectDetailResource resource, String username)
            throws CloneNotSupportedException {

        ProjectDetail projectDetail =
                projectDetailRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        // Object oldRejectByPFS = projectProposal.clone();

        projectDetail.setProjectName(resource.getProjectName());
        projectDetail.setBorrowerName(resource.getBorrowerName());
        projectDetail.setPromoterName(resource.getPromoterName());
        projectDetail.setLoanPurpose(resource.getLoanPurpose());
        projectDetail.setProjectCapacity(resource.getProjectCapacity());
        projectDetail.setProjectCapacityUnit(resource.getProjectCapacityUnit());
        projectDetail.setState(resource.getState());
        projectDetail.setDistrict(resource.getDistrict());
        projectDetail.setProductType(resource.getProductType());
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

        return projectDetail;
    }
}
