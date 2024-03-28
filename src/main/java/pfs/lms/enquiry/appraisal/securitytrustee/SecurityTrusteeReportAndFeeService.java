package pfs.lms.enquiry.appraisal.securitytrustee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityTrusteeReportAndFeeService implements ISecurityTrusteeReportAndFeeService
{
    private final SecurityTrusteeRepository securityTrusteeRepository;
    private final SecurityTrusteeReportAndFeeRepository securityTrusteeReportAndFeeRepository;

    @Override
    public SecurityTrusteeReportAndFee saveReportAndFee(SecurityTrusteeReportAndFeeResource resource, String username)
    {
        SecurityTrustee securityTrustee = securityTrusteeRepository.getOne(resource.getSecurityTrusteeId());
        SecurityTrusteeReportAndFee reportAndFee = resource.getSecurityTrusteeReportAndFee();
        reportAndFee.setSerialNumber(securityTrusteeReportAndFeeRepository.findBySecurityTrustee(securityTrustee).size() + 1);
        reportAndFee.setSecurityTrustee(securityTrustee);
        reportAndFee = securityTrusteeReportAndFeeRepository.save(reportAndFee);

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(reportAndFee.getLendersIndependentEngineer().getLoanMonitor(),
//                reportAndFee.getLendersIndependentEngineer().getLoanAppraisal(),resource.getModuleName());
//
//        // Create Change Document for LIE Report And Fee
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                reportAndFee.getId(),
//                lendersIndependentEngineer.getId(),
//                lendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                reportAndFee,
//                "Created",
//                username,
//                resource.getModuleName() , "LIE Report And Fee" );

        return reportAndFee;
    }

    @Override
    public SecurityTrusteeReportAndFee updateReportAndFee(SecurityTrusteeReportAndFeeResource resource, String username)
            throws CloneNotSupportedException
    {
        SecurityTrusteeReportAndFee reportAndFee = securityTrusteeReportAndFeeRepository
                .getOne(resource.getSecurityTrusteeReportAndFee().getId());

        Object oldReportAndFee = reportAndFee.clone();

        reportAndFee.setReportType(resource.getSecurityTrusteeReportAndFee().getReportType());
        reportAndFee.setDateOfReceipt(resource.getSecurityTrusteeReportAndFee().getDateOfReceipt());
        reportAndFee.setInvoiceDate(resource.getSecurityTrusteeReportAndFee().getInvoiceDate());
        reportAndFee.setInvoiceNo(resource.getSecurityTrusteeReportAndFee().getInvoiceNo());
        reportAndFee.setFeeAmount(resource.getSecurityTrusteeReportAndFee().getFeeAmount());
        reportAndFee.setStatusOfFeeReceipt(resource.getSecurityTrusteeReportAndFee().getStatusOfFeeReceipt());
        reportAndFee.setStatusOfFeePaid(resource.getSecurityTrusteeReportAndFee().getStatusOfFeePaid());
        reportAndFee.setDocumentTitle(resource.getSecurityTrusteeReportAndFee().getDocumentTitle());
        reportAndFee.setDocumentType(resource.getSecurityTrusteeReportAndFee().getDocumentType());
        reportAndFee.setNextReportDate(resource.getSecurityTrusteeReportAndFee().getNextReportDate());
        reportAndFee.setFileReference(resource.getSecurityTrusteeReportAndFee().getFileReference());

        reportAndFee.setReportDate(resource.getSecurityTrusteeReportAndFee().getReportDate());
        reportAndFee.setPercentageCompletion(resource.getSecurityTrusteeReportAndFee().getPercentageCompletion());
        reportAndFee.setRemarks(resource.getSecurityTrusteeReportAndFee().getRemarks());

        reportAndFee = securityTrusteeReportAndFeeRepository.save(reportAndFee);

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(reportAndFee.getLendersIndependentEngineer().getLoanMonitor(),
//                reportAndFee.getLendersIndependentEngineer().getLoanAppraisal(),resource.getModuleName());
//
//        // Create Change Document for LIE Report And Fee
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                reportAndFee.getId(),reportAndFee.getLendersIndependentEngineer().getId(),
//                reportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldReportAndFee,
//                reportAndFee,
//                "Updated",
//                username,
//                resource.getModuleName() , "LIE Report And Fee" );

        return reportAndFee;
    }

    @Override
    public SecurityTrusteeReportAndFee deleteReportAndFee(UUID reportAndFeeId, String moduleName, String username) {
        SecurityTrusteeReportAndFee reportAndFee = securityTrusteeReportAndFeeRepository.getOne(reportAndFeeId.toString());

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(reportAndFee.getLendersIndependentEngineer().getLoanMonitor(),
//                reportAndFee.getLendersIndependentEngineer().getLoanAppraisal(),moduleName);
//
//        // Create Change Document for LIE Delete
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                reportAndFee.getId(),
//                null,
//                reportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                reportAndFee,
//                "Deleted",
//                username,
//                moduleName, "LIE Report And Fee" );

        securityTrusteeReportAndFeeRepository.delete(reportAndFee);
        updateSerialNumbers(reportAndFee.getSecurityTrustee().getId());
        return reportAndFee;
    }

    @Override
    public List<SecurityTrusteeReportAndFeeResource> getReportAndFees(String securityTrusteeId, String name) {
        List<SecurityTrusteeReportAndFeeResource> securityTrusteeReportAndFeeResources  = new ArrayList<>();
        SecurityTrustee securityTrustee = securityTrusteeRepository.getOne(securityTrusteeId);
        List<SecurityTrusteeReportAndFee> reportAndFees = securityTrusteeReportAndFeeRepository.findBySecurityTrustee(securityTrustee);
        reportAndFees.forEach(
                reportAndFee -> {
                    SecurityTrusteeReportAndFeeResource resource = new SecurityTrusteeReportAndFeeResource();
                    resource.setSecurityTrusteeId(securityTrustee.getId());
                    resource.setSecurityTrusteeReportAndFee(reportAndFee);
                    securityTrusteeReportAndFeeResources.add(resource);
                }
        );
        Collections.sort(securityTrusteeReportAndFeeResources, Comparator.comparingInt((SecurityTrusteeReportAndFeeResource a) ->
                a.getSecurityTrusteeReportAndFee().getSerialNumber()).reversed());
        return securityTrusteeReportAndFeeResources;

    }

    private void updateSerialNumbers(String securityTrusteeId) {
        SecurityTrustee securityTrustee = securityTrusteeRepository.getOne(securityTrusteeId);
        List<SecurityTrusteeReportAndFee> securityTrusteeReportAndFees = securityTrusteeReportAndFeeRepository.findBySecurityTrustee(securityTrustee);
        int size = securityTrusteeReportAndFees.size();
        for(SecurityTrusteeReportAndFee securityTrusteeReportAndFee: securityTrusteeReportAndFees) {
            if (securityTrusteeReportAndFee.getSerialNumber() != size) {
                securityTrusteeReportAndFee.setSerialNumber(size);
                securityTrusteeReportAndFeeRepository.save(securityTrusteeReportAndFee);
            }
            size--;
        }
    }
}
