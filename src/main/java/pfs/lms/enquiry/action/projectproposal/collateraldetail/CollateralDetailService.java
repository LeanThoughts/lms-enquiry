package pfs.lms.enquiry.action.projectproposal.collateraldetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.projectproposal.*;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollateralDetailService implements ICollateralDetailService {

    private final IChangeDocumentService changeDocumentService;

    private final ProjectProposalRepository projectProposalRepository;
    private final CollateralDetailRepository collateralDetailRepository;

    @Override
    public CollateralDetail create(CollateralDetailResource resource, String username) {
        ProjectProposal projectProposal = projectProposalRepository.getOne(resource.getProjectProposalId());
        CollateralDetail collateralDetail = new CollateralDetail();
        collateralDetail.setProjectProposal(projectProposal);
        collateralDetail.setCollateralType(resource.getCollateralType());
        collateralDetail.setDetails(resource.getDetails());
        collateralDetail = collateralDetailRepository.save(collateralDetail);

        // Change Documents for Collateral Detail
        changeDocumentService.createChangeDocument(
                projectProposal.getEnquiryAction().getId(),
                projectProposal.getId().toString(),
                projectProposal.getEnquiryAction().getId().toString(),
                projectProposal.getEnquiryAction().getLoanApplication().getLoanContractId(),
                null,
                collateralDetail,
                "Created",
                username,
                "EnquiryAction", "Collateral Detail" );

        return collateralDetail;
    }

    @Override
    public CollateralDetail update(CollateralDetailResource resource, String username)
            throws CloneNotSupportedException {

        CollateralDetail collateralDetail =
                collateralDetailRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

      Object oldCollateralDetail = collateralDetail.clone();

        collateralDetail.setCollateralType(resource.getCollateralType());
        collateralDetail.setDetails(resource.getDetails());
        collateralDetail = collateralDetailRepository.save(collateralDetail);

        // Change Documents for Collateral Detail
        changeDocumentService.createChangeDocument(
                collateralDetail.getProjectProposal().getId(),
                collateralDetail.getId().toString(),
                collateralDetail.getProjectProposal().getId().toString(),
                collateralDetail.getProjectProposal().getEnquiryAction().getLoanApplication().getLoanContractId(),
                oldCollateralDetail,
                collateralDetail,
                "Updated",
                username,
                "EnquiryAction", "Collateral Detail" );

        return collateralDetail;
    }
}
