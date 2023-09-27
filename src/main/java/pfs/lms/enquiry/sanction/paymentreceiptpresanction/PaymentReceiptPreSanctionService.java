package pfs.lms.enquiry.sanction.paymentreceiptpresanction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.sanction.Sanction;
import pfs.lms.enquiry.sanction.SanctionRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentReceiptPreSanctionService implements IPaymentReceiptPreSanctionService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final SanctionRepository sanctionRepository;
    private final PaymentReceiptPreSanctionRepository paymentReceiptPreSanctionRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public PaymentReceiptPreSanction createPaymentReceipt(PaymentReceiptPreSanctionResource resource, String username) {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        Sanction sanction = sanctionRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    Sanction obj = new Sanction();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = sanctionRepository.save(obj);
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
        PaymentReceiptPreSanction paymentReceiptPreSanction = new PaymentReceiptPreSanction();
        paymentReceiptPreSanction.setPayee(resource.getPayee());
        paymentReceiptPreSanction.setAmount(resource.getAmount());
        paymentReceiptPreSanction.setAmountReceived(resource.getAmountReceived());
        paymentReceiptPreSanction.setSanction(sanction);
        paymentReceiptPreSanction.setFeeInvoice(resource.getFeeInvoice());
        paymentReceiptPreSanction.setDateOfTransfer(resource.getDateOfTransfer());
        paymentReceiptPreSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPreSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPreSanction.setReferenceNumber(resource.getReferenceNumber());
        paymentReceiptPreSanction.setRtgsNeftNumber(resource.getRtgsNeftNumber());
        paymentReceiptPreSanction = paymentReceiptPreSanctionRepository.save(paymentReceiptPreSanction);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                boardApprovalReasonForDelay.getBoardApproval().getId(),
//                boardApprovalReasonForDelay.getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getLoanContractId(),
//                null,
//                boardApprovalReasonForDelay,
//                "Created",
//                username,
//                "Appraisal", "Reason For Delay" );

        return paymentReceiptPreSanction;
    }

    @Override
    public PaymentReceiptPreSanction updatePaymentReceipt(PaymentReceiptPreSanctionResource resource, String username) throws CloneNotSupportedException {
        PaymentReceiptPreSanction paymentReceiptPreSanction =
                paymentReceiptPreSanctionRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = paymentReceiptPreSanction.clone();

        paymentReceiptPreSanction.setPayee(resource.getPayee());
        paymentReceiptPreSanction.setAmount(resource.getAmount());
        paymentReceiptPreSanction.setAmountReceived(resource.getAmountReceived());
        paymentReceiptPreSanction.setFeeInvoice(resource.getFeeInvoice());
        paymentReceiptPreSanction.setDateOfTransfer(resource.getDateOfTransfer());
        paymentReceiptPreSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPreSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPreSanction.setReferenceNumber(resource.getReferenceNumber());
        paymentReceiptPreSanction.setRtgsNeftNumber(resource.getRtgsNeftNumber());
        paymentReceiptPreSanction = paymentReceiptPreSanctionRepository.save(paymentReceiptPreSanction);

        // Change Documents for Reason Delay
//        changeDocumentService.createChangeDocument(
//                boardApprovalReasonForDelay.getBoardApproval().getId(),
//                boardApprovalReasonForDelay.getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getId().toString(),
//                boardApprovalReasonForDelay.getBoardApproval().getLoanApplication().getLoanContractId(),
//                oldReasonForDelay,
//                boardApprovalReasonForDelay,
//                "Updated",
//                username,
//                "Appraisal", "Reason For Delay" );

        return paymentReceiptPreSanction;
    }

    @Override
    public PaymentReceiptPreSanction deletePaymentReceipt(UUID paymentReceiptId) throws CloneNotSupportedException {
        PaymentReceiptPreSanction paymentReceiptPreSanction = paymentReceiptPreSanctionRepository.findById(paymentReceiptId).
                orElseThrow(() -> new EntityNotFoundException(paymentReceiptId.toString()));
        paymentReceiptPreSanctionRepository.delete(paymentReceiptPreSanction);
        return paymentReceiptPreSanction;
    }
}
