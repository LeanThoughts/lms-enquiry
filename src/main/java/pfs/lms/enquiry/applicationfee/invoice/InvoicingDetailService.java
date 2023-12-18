package pfs.lms.enquiry.applicationfee.invoice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.applicationfee.ApplicationFee;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.iccapproval.ICCApproval;
import pfs.lms.enquiry.iccapproval.ICCApprovalRepository;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICC;
import pfs.lms.enquiry.iccapproval.approvalbyicc.ApprovalByICCRepository;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetail;
import pfs.lms.enquiry.iccapproval.iccfurtherdetail.ICCFurtherDetailRepository;
import pfs.lms.enquiry.iccapproval.loanenhancement.LoanEnhancement;
import pfs.lms.enquiry.iccapproval.loanenhancement.LoanEnhancementRepository;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomer;
import pfs.lms.enquiry.iccapproval.rejectedbycustomer.RejectedByCustomerRepository;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICC;
import pfs.lms.enquiry.iccapproval.rejectedbyicc.RejectedByICCRepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvoicingDetailService implements IInvoicingDetailService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationFeeRepository applicationFeeRepository;
    private final InvoicingDetailRepository invoicingDetailRepository;

    private final ICCApprovalRepository iccApprovalRepository;
    private final ICCFurtherDetailRepository furtherDetailRepository;
    private final RejectedByICCRepository rejectedByICCRepository;
    private final ApprovalByICCRepository approvalByICCRepository;
    private final LoanEnhancementRepository loanEnhancementRepository;
    private final RejectedByCustomerRepository rejectedByCustomerRepository;

    private final IChangeDocumentService changeDocumentService;

    @Override
    public InvoicingDetail create(InvoicingDetailResource invoicingDetailResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(invoicingDetailResource.getLoanApplicationId());

        ApplicationFee applicationFee = applicationFeeRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    ApplicationFee obj = new ApplicationFee();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = applicationFeeRepository.save(obj);

                    // Change Documents for Appraisal Header
//                    changeDocumentService.createChangeDocument(
//                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
//                            loanApplication.getLoanContractId(),
//                            null,
//                            obj,
//                            "Created",
//                            username,
//                            "Appraisal", "Header");

                    return obj;
                });

        InvoicingDetail invoicingDetail = new InvoicingDetail();
        invoicingDetail.setApplicationFee(applicationFee);
        invoicingDetail.setIccMeetingNumber(invoicingDetailResource.getIccMeetingNumber());
        invoicingDetail.setCompanyName(invoicingDetailResource.getCompanyName());
        invoicingDetail.setCinNumber(invoicingDetailResource.getCinNumber());
        invoicingDetail.setGstNumber(invoicingDetailResource.getGstNumber());
        invoicingDetail.setPan(invoicingDetailResource.getPan());
        invoicingDetail.setMsmeRegistrationNumber(invoicingDetailResource.getMsmeRegistrationNumber());
        invoicingDetail.setDoorNumber(invoicingDetailResource.getDoorNumber());
        invoicingDetail.setAddress(invoicingDetailResource.getAddress());
        invoicingDetail.setStreet(invoicingDetailResource.getStreet());
        invoicingDetail.setCity(invoicingDetailResource.getCity());
        invoicingDetail.setState(invoicingDetailResource.getState());
        invoicingDetail.setPostalCode(invoicingDetailResource.getPostalCode());
        invoicingDetail.setLandline(invoicingDetailResource.getLandline());
        invoicingDetail.setMobile(invoicingDetailResource.getMobile());
        invoicingDetail.setEmail(invoicingDetailResource.getEmail());
        invoicingDetail.setProjectType(invoicingDetailResource.getProjectType());
        invoicingDetail.setPfsDebtAmount(invoicingDetailResource.getPfsDebtAmount());
        invoicingDetail.setProjectCapacity(invoicingDetailResource.getProjectCapacity());
        invoicingDetail.setProjectCapacityUnit(invoicingDetailResource.getProjectCapacityUnit());
        invoicingDetail.setProjectLocationState(invoicingDetailResource.getProjectLocationState());
        invoicingDetail.setFileReference(invoicingDetailResource.getFileReference());
        invoicingDetail = invoicingDetailRepository.save(invoicingDetail);
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanApplication.getLoanContractId(),
//                null,
//                loanPartner,
//                "Created",
//                username,
//                "Appraisal", "Loan Partner");

        return invoicingDetail;
    }

    @Override
    public InvoicingDetail update(InvoicingDetailResource invoicingDetailResource, String username)
            throws CloneNotSupportedException {

        InvoicingDetail invoicingDetail = invoicingDetailRepository.findById(invoicingDetailResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(invoicingDetailResource.getId().toString()));

        Object oldFormalRequest = invoicingDetail.clone();

        invoicingDetail.setIccMeetingNumber(invoicingDetailResource.getIccMeetingNumber());
        invoicingDetail.setCompanyName(invoicingDetailResource.getCompanyName());
        invoicingDetail.setCinNumber(invoicingDetailResource.getCinNumber());
        invoicingDetail.setGstNumber(invoicingDetailResource.getGstNumber());
        invoicingDetail.setPan(invoicingDetailResource.getPan());
        invoicingDetail.setMsmeRegistrationNumber(invoicingDetailResource.getMsmeRegistrationNumber());
        invoicingDetail.setDoorNumber(invoicingDetailResource.getDoorNumber());
        invoicingDetail.setAddress(invoicingDetailResource.getAddress());
        invoicingDetail.setStreet(invoicingDetailResource.getStreet());
        invoicingDetail.setCity(invoicingDetailResource.getCity());
        invoicingDetail.setState(invoicingDetailResource.getState());
        invoicingDetail.setPostalCode(invoicingDetailResource.getPostalCode());
        invoicingDetail.setLandline(invoicingDetailResource.getLandline());
        invoicingDetail.setMobile(invoicingDetailResource.getMobile());
        invoicingDetail.setEmail(invoicingDetailResource.getEmail());
        invoicingDetail.setProjectType(invoicingDetailResource.getProjectType());
        invoicingDetail.setPfsDebtAmount(invoicingDetailResource.getPfsDebtAmount());
        invoicingDetail.setProjectCapacity(invoicingDetailResource.getProjectCapacity());
        invoicingDetail.setProjectCapacityUnit(invoicingDetailResource.getProjectCapacityUnit());
        invoicingDetail.setProjectLocationState(invoicingDetailResource.getProjectLocationState());
        invoicingDetail.setFileReference(invoicingDetailResource.getFileReference());
        invoicingDetail = invoicingDetailRepository.save(invoicingDetail);

        // Change Documents for  Loan Partner
//        changeDocumentService.createChangeDocument(
//                loanAppraisalForPartner.getId(),
//                loanPartner.getId().toString(),
//                loanAppraisalForPartner.getId().toString(),
//                loanPartner.getLoanApplication().getLoanContractId(),
//                oldLoanPartner,
//                loanPartner,
//                "Updated",
//                username,
//                "Appraisal", "Loan Partner");

        return invoicingDetail;
    }

    @Override
    public List<String> getICCMeetingNumbers(UUID loanApplicationId) {
        ICCApproval iccApproval = iccApprovalRepository.findByLoanApplicationId(loanApplicationId);

        List<String> meetingNumbers = new ArrayList<>();

        List<ICCFurtherDetail> furtherDetails = furtherDetailRepository.findByIccApprovalId(iccApproval.getId());
        furtherDetails.forEach(furtherDetail -> {
            meetingNumbers.add(furtherDetail.getIccMeetingNumber());
        });

        RejectedByICC rejectedByICC = rejectedByICCRepository.findByIccApprovalId(iccApproval.getId());
        if (rejectedByICC != null)
            meetingNumbers.add(rejectedByICC.getMeetingNumber());

        ApprovalByICC approvalByICC = approvalByICCRepository.findByIccApprovalId(iccApproval.getId());
        if (approvalByICC != null)
            meetingNumbers.add(approvalByICC.getMeetingNumber());

        RejectedByCustomer rejectedByCustomer = rejectedByCustomerRepository.findByIccApprovalId(iccApproval.getId());
        if (rejectedByCustomer != null)
            meetingNumbers.add(rejectedByCustomer.getMeetingNumber());

        List<LoanEnhancement> loanEnhancements = loanEnhancementRepository.findByIccApprovalId(iccApproval.getId());
        loanEnhancements.forEach(loanEnhancement -> {
            meetingNumbers.add(loanEnhancement.getIccMeetingNumber());
        });

        return meetingNumbers;
    }
}
