package pfs.lms.enquiry.applicationfee.applicationfee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.applicationfee.ApplicationFeeRepository;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InceptionFeeService implements IInceptionFeeService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final ApplicationFeeRepository applicationFeeRepository;
    private final InceptionFeeRepository inceptionFeeRepository;
    private final IChangeDocumentService changeDocumentService;

    @Override
    public InceptionFee create(InceptionFeeResource inceptionFeeResource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(inceptionFeeResource.getLoanApplicationId());

        pfs.lms.enquiry.applicationfee.ApplicationFee applicationFee = applicationFeeRepository.findByLoanApplication(loanApplication)
                .orElseGet(() -> {
                    pfs.lms.enquiry.applicationfee.ApplicationFee obj = new pfs.lms.enquiry.applicationfee.ApplicationFee();
                    obj.setLoanApplication(loanApplication);
                    obj.setLoanContractId(loanApplication.getLoanContractId());
                    obj = applicationFeeRepository.save(obj);

                    // Change Documents for Application Fee Header
                    changeDocumentService.createChangeDocument(
                            obj.getId(),obj.getId().toString(),obj.getId().toString(),
                            loanApplication.getLoanContractId(),
                            null,
                            obj,
                            "Created",
                            username,
                            "Application Fee", "Header");

                    return obj;
                });

        InceptionFee inceptionFee = new InceptionFee();
        inceptionFee.setApplicationFee(applicationFee);
        inceptionFee.setInvoiceNumber(inceptionFeeResource.getInvoiceNumber());
        inceptionFee.setInvoiceDate(inceptionFeeResource.getInvoiceDate());
        inceptionFee.setAmount(inceptionFeeResource.getAmount());
        inceptionFee.setTaxAmount(inceptionFeeResource.getTaxAmount());
        inceptionFee.setTotalAmount(inceptionFeeResource.getTotalAmount());
        inceptionFee.setAmountReceived(inceptionFeeResource.getAmountReceived());
        inceptionFee.setRtgsNumber(inceptionFeeResource.getRtgsNumber());
        inceptionFee.setReferenceNumber(inceptionFeeResource.getReferenceNumber());
        inceptionFee.setRemarks(inceptionFeeResource.getRemarks());

        inceptionFee.setHeaderDocumentNumber(inceptionFeeResource.getHeaderDocumentNumber());
        inceptionFee.setStatusCode(inceptionFeeResource.getStatusCode());
        inceptionFee.setStatusDescription(inceptionFeeResource.getStatusDescription());
        inceptionFee.setDescription(inceptionFeeResource.getDescription());
        inceptionFee.setLoanContractId(inceptionFeeResource.getLoanContractId());

        inceptionFee = inceptionFeeRepository.save(inceptionFee);
        changeDocumentService.createChangeDocument(
                inceptionFee.getId(),
                inceptionFee.getId().toString(),
                inceptionFee.getId().toString(),
                loanApplication.getLoanContractId(),
                null,
                inceptionFee,
                "Created",
                username,
                "Application Fee", "Application Fee");

        return inceptionFee;
    }

    @Override
    public InceptionFee update(InceptionFeeResource inceptionFeeResource, String username)
            throws CloneNotSupportedException {

        InceptionFee inceptionFee = inceptionFeeRepository.findById(inceptionFeeResource.getId())
                .orElseThrow(() -> new EntityNotFoundException(inceptionFeeResource.getId().toString()));

        Object oldInceptionFee = inceptionFee.clone();

        inceptionFee.setInvoiceNumber(inceptionFeeResource.getInvoiceNumber());
        inceptionFee.setInvoiceDate(inceptionFeeResource.getInvoiceDate());
        inceptionFee.setAmount(inceptionFeeResource.getAmount());
        inceptionFee.setTaxAmount(inceptionFeeResource.getTaxAmount());
        inceptionFee.setTotalAmount(inceptionFeeResource.getTotalAmount());
        inceptionFee.setAmountReceived(inceptionFeeResource.getAmountReceived());
        inceptionFee.setRtgsNumber(inceptionFeeResource.getRtgsNumber());
        inceptionFee.setReferenceNumber(inceptionFeeResource.getReferenceNumber());
        inceptionFee.setRemarks(inceptionFeeResource.getRemarks());
        inceptionFee.setHeaderDocumentNumber(inceptionFeeResource.getHeaderDocumentNumber());
        inceptionFee.setStatusCode(inceptionFeeResource.getStatusCode());
        inceptionFee.setStatusDescription(inceptionFeeResource.getStatusDescription());
        inceptionFee.setDescription(inceptionFeeResource.getDescription());
        inceptionFee.setLoanContractId(inceptionFeeResource.getLoanContractId());

        inceptionFee = inceptionFeeRepository.save(inceptionFee);

        // Change Documents for  Loan Partner
        changeDocumentService.createChangeDocument(
                inceptionFee.getId(),
                inceptionFee.getId().toString(),
                inceptionFee.getId().toString(),
                inceptionFee.getLoanContractId(),
                oldInceptionFee,
                inceptionFee,
                "Created",
                username,
                "Application Fee", "Application Fee");

        return inceptionFee;
    }

    @Override
    public InceptionFee delete(UUID inceptionFeeId, String username) {
        InceptionFee inceptionFee = inceptionFeeRepository.findById(inceptionFeeId)
                .orElseThrow(() -> new EntityNotFoundException(inceptionFeeId.toString()));
        inceptionFeeRepository.delete(inceptionFee);
        return inceptionFee;
    }
}
