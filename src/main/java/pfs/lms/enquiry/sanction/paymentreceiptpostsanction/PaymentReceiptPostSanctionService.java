package pfs.lms.enquiry.sanction.paymentreceiptpostsanction;

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
public class PaymentReceiptPostSanctionService implements IPaymentReceiptPostSanctionService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final SanctionRepository sanctionRepository;
    private final PaymentReceiptPostSanctionRepository paymentReceiptPostSanctionRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public PaymentReceiptPostSanction createPaymentReceipt(PaymentReceiptPostSanctionResource resource, String username) {
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
        PaymentReceiptPostSanction paymentReceiptPostSanction = new PaymentReceiptPostSanction();
        paymentReceiptPostSanction.setPayee(resource.getPayee());
        paymentReceiptPostSanction.setAmount(resource.getAmount());
        paymentReceiptPostSanction.setAmountReceived(resource.getAmountReceived());
        paymentReceiptPostSanction.setSanction(sanction);
        paymentReceiptPostSanction.setFeeInvoice(resource.getFeeInvoice());
        paymentReceiptPostSanction.setDateOfTransfer(resource.getDateOfTransfer());
        paymentReceiptPostSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPostSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPostSanction.setReferenceNumber(resource.getReferenceNumber());
        paymentReceiptPostSanction.setRtgsNeftNumber(resource.getRtgsNeftNumber());
        paymentReceiptPostSanction = paymentReceiptPostSanctionRepository.save(paymentReceiptPostSanction);

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

        return paymentReceiptPostSanction;
    }

    @Override
    public PaymentReceiptPostSanction updatePaymentReceipt(PaymentReceiptPostSanctionResource resource, String username) throws CloneNotSupportedException {
        PaymentReceiptPostSanction paymentReceiptPostSanction =
                paymentReceiptPostSanctionRepository.findById(resource.getId())
                        .orElseThrow(() -> new EntityNotFoundException(resource.getId().toString()));

        Object oldReasonForDelay = paymentReceiptPostSanction.clone();

        paymentReceiptPostSanction.setPayee(resource.getPayee());
        paymentReceiptPostSanction.setAmount(resource.getAmount());
        paymentReceiptPostSanction.setAmountReceived(resource.getAmountReceived());
        paymentReceiptPostSanction.setFeeInvoice(resource.getFeeInvoice());
        paymentReceiptPostSanction.setDateOfTransfer(resource.getDateOfTransfer());
        paymentReceiptPostSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPostSanction.setProformaInvoiceDate(resource.getProformaInvoiceDate());
        paymentReceiptPostSanction.setReferenceNumber(resource.getReferenceNumber());
        paymentReceiptPostSanction.setRtgsNeftNumber(resource.getRtgsNeftNumber());
        paymentReceiptPostSanction = paymentReceiptPostSanctionRepository.save(paymentReceiptPostSanction);

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

        return paymentReceiptPostSanction;
    }

    @Override
    public PaymentReceiptPostSanction deletePaymentReceipt(UUID paymentReceiptId) throws CloneNotSupportedException {
        PaymentReceiptPostSanction paymentReceiptPostSanction = paymentReceiptPostSanctionRepository.findById(paymentReceiptId).
                orElseThrow(() -> new EntityNotFoundException(paymentReceiptId.toString()));
        paymentReceiptPostSanctionRepository.delete(paymentReceiptPostSanction);
        return paymentReceiptPostSanction;
    }
}
