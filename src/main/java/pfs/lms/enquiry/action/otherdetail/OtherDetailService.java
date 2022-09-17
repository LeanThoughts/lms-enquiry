package pfs.lms.enquiry.action.otherdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.action.EnquiryAction;
import pfs.lms.enquiry.action.EnquiryActionRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtherDetailService implements IOtherDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final EnquiryActionRepository enquiryActionRepository;
    private final OtherDetailRepository otherDetailRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public OtherDetail create(OtherDetailResource resource, String username) {
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
        OtherDetail otherDetail = new OtherDetail();
        otherDetail.setEnquiryAction(enquiryAction);
        otherDetail.setNameOfSourcingCompany(resource.getNameOfSourcingCompany());
        otherDetail.setContactPersonName(resource.getContactPersonName());
        otherDetail.setContactNumber(resource.getContactNumber());
        otherDetail.setEmail(resource.getEmail());
        otherDetail.setEnquiryDate(resource.getEnquiryDate());
        otherDetail.setRating(resource.getRating());
        otherDetail.setCreditStanding(resource.getCreditStanding());
        otherDetail.setCreditStandingInstruction(resource.getCreditStandingInstruction());
        otherDetail.setCreditStandingText(resource.getCreditStandingText());
        otherDetail = otherDetailRepository.save(otherDetail);

        // Change Documents for Other Detail
        changeDocumentService.createChangeDocument(
                otherDetail.getEnquiryAction().getId(),
                otherDetail.getId().toString(),
                otherDetail.getEnquiryAction().getId().toString(),
                otherDetail.getEnquiryAction().getLoanApplication().getLoanContractId(),
                null,
                otherDetail,
                "Created",
                username,
                "EnquiryAction", "Other Detail" );

        return otherDetail;
    }

    @Override
    public OtherDetail update(OtherDetailResource resource, String username)
            throws CloneNotSupportedException {
        OtherDetail otherDetail =
                otherDetailRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldRejectByPFS = otherDetail.clone();

        otherDetail.setNameOfSourcingCompany(resource.getNameOfSourcingCompany());
        otherDetail.setContactPersonName(resource.getContactPersonName());
        otherDetail.setContactNumber(resource.getContactNumber());
        otherDetail.setEmail(resource.getEmail());
        otherDetail.setEnquiryDate(resource.getEnquiryDate());
        otherDetail.setRating(resource.getRating());
        otherDetail.setCreditStanding(resource.getCreditStanding());
        otherDetail.setCreditStandingInstruction(resource.getCreditStandingInstruction());
        otherDetail.setCreditStandingText(resource.getCreditStandingText());
        otherDetail = otherDetailRepository.save(otherDetail);

        // Change Documents for Other Detail
        changeDocumentService.createChangeDocument(
                otherDetail.getEnquiryAction().getId(),
                otherDetail.getId().toString(),
                otherDetail.getEnquiryAction().getId().toString(),
                otherDetail.getEnquiryAction().getLoanApplication().getLoanContractId(),
                oldRejectByPFS,
                otherDetail,
                "Updated",
                username,
                "EnquiryAction", "Other Detail");

        return otherDetail;
    }
}
