package pfs.lms.enquiry.appraisal.securitytrustee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.appraisal.LoanAppraisal;
import pfs.lms.enquiry.appraisal.LoanAppraisalRepository;
import pfs.lms.enquiry.appraisal.service.LoanAppraisalService;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.repository.LoanApplicationRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityTrusteeService implements ISecurityTrusteeService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAppraisalService loanAppraisalService;
    private final SecurityTrusteeRepository securityTrusteeRepository;
private final LoanAppraisalRepository loanAppraisalRepository;

    @Override
    public SecurityTrustee saveSecurityTrustee(SecurityTrusteeResource resource, String username)
            throws CloneNotSupportedException
    {
        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanAppraisal loanAppraisal = loanAppraisalService.createLoanAppraisal(loanApplication, username);

        SecurityTrustee securityTrustee = resource.getSecurityTrustee();
        securityTrustee.setSerialNumber(securityTrusteeRepository.findByLoanAppraisal(loanAppraisal).size() + 1);
        securityTrustee.setLoanAppraisal(loanAppraisal);
        securityTrustee.setAdvisor(resource.getSecurityTrustee().getAdvisor());
        securityTrustee.setBpCode(resource.getSecurityTrustee().getBpCode());
        securityTrustee.setName(resource.getSecurityTrustee().getName());
        securityTrustee.setDateOfAppointment(resource.getSecurityTrustee().getDateOfAppointment());
        securityTrustee.setContractPeriodFrom(resource.getSecurityTrustee().getContractPeriodFrom());
        securityTrustee.setContractPeriodTo(resource.getSecurityTrustee().getContractPeriodTo());
        securityTrustee.setContactPerson(resource.getSecurityTrustee().getContactPerson());
        securityTrustee.setContactNumber(resource.getSecurityTrustee().getContactNumber());
        securityTrustee.setEmail(resource.getSecurityTrustee().getEmail());
        securityTrustee = securityTrusteeRepository.save(securityTrustee);

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(securityTrustee.getLoanMonitor(),
//                securityTrustee.getLoanAppraisal(),resource.getModuleName());
//
//        // Create Change Document for LIE
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                securityTrustee.getId(),
//                null,
//                loanApplication.getLoanContractId(),
//                null,
//                securityTrustee,
//                "Created",
//                username,
//                resource.getModuleName() , "Lenders Independent Engineer" );

        return securityTrustee;
    }

    @Override
    public SecurityTrustee updateSecurityTrustee(SecurityTrusteeResource resource, String username)
            throws CloneNotSupportedException
    {
        SecurityTrustee securityTrustee = securityTrusteeRepository.getOne(resource.getSecurityTrustee().getId());

        //Clone the LIE Object for Change Document
        Object oldLendersIndependentEngineer = securityTrustee.clone();

        securityTrustee.setAdvisor(resource.getSecurityTrustee().getAdvisor());
        securityTrustee.setBpCode(resource.getSecurityTrustee().getBpCode());
        securityTrustee.setName(resource.getSecurityTrustee().getName());
        securityTrustee.setDateOfAppointment(resource.getSecurityTrustee().getDateOfAppointment());
        securityTrustee.setContractPeriodFrom(resource.getSecurityTrustee().getContractPeriodFrom());
        securityTrustee.setContractPeriodTo(resource.getSecurityTrustee().getContractPeriodTo());
        securityTrustee.setContactPerson(resource.getSecurityTrustee().getContactPerson());
        securityTrustee.setContactNumber(resource.getSecurityTrustee().getContactNumber());
        securityTrustee.setEmail(resource.getSecurityTrustee().getEmail());
        securityTrustee = securityTrusteeRepository.save(securityTrustee);

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(securityTrustee.getLoanMonitor(),
//                securityTrustee.getLoanAppraisal(),resource.getModuleName());
//
//        //Create Change Document
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                securityTrustee.getId(),
//                null,
//                securityTrustee.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                oldLendersIndependentEngineer,
//                securityTrustee,
//                "Updated",
//                username,
//                resource.getModuleName(), "Lenders Independent Engineer" );

        return securityTrustee;
    }

    @Override
    public SecurityTrustee deleteSecurityTrustee(UUID securityTrusteeId, String moduleName, String username)
    {
        SecurityTrustee securityTrustee = securityTrusteeRepository.getOne(securityTrusteeId.toString());
        LoanAppraisal loanAppraisal = securityTrustee.getLoanAppraisal();

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(securityTrustee.getLoanMonitor(),
//                securityTrustee.getLoanAppraisal(), moduleName);
//
//        // Create Change Document for LIE Delete
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                securityTrustee.getId(),
//                null,
//                securityTrustee.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                securityTrustee,
//                "Deleted",
//                username,
//                moduleName, "Lenders Independent Engineer" );

        securityTrusteeRepository.delete(securityTrustee);
        updateSerialNumbers(loanAppraisal);

        return securityTrustee;
    }

    @Override
    public List<SecurityTrusteeResource> getSecurityTrustees(String loanApplicationId, String name)
    {
        List<SecurityTrusteeResource> securityTrusteeResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanAppraisal loanAppraisal = loanAppraisalRepository.findByLoanApplication(loanApplication)
                .orElseThrow(() -> new EntityNotFoundException(loanApplicationId.toString()));
        if(loanAppraisal != null) {
            List<SecurityTrustee> securityTrustees = securityTrusteeRepository.findByLoanAppraisal(loanAppraisal);
            securityTrustees.forEach(
                    securityTrustee -> {
                        SecurityTrusteeResource securityTrusteeResource = new SecurityTrusteeResource();
                        securityTrusteeResource.setLoanApplicationId(loanApplication.getId());
                        securityTrusteeResource.setSecurityTrustee(securityTrustee);
                        securityTrusteeResources.add(securityTrusteeResource);
                    }
            );
        }
        Collections.sort(securityTrusteeResources, Comparator.comparingInt((SecurityTrusteeResource a) ->
                a.getSecurityTrustee().getSerialNumber()).reversed());
        return securityTrusteeResources;
    }

    private void updateSerialNumbers(LoanAppraisal loanAppraisal) {
        List<SecurityTrustee> securityTrustees = securityTrusteeRepository.findByLoanAppraisal(loanAppraisal);
        int size = securityTrustees.size();
        for(SecurityTrustee securityTrustee: securityTrustees) {
            if (securityTrustee.getSerialNumber() != size) {
                securityTrustee.setSerialNumber(size);
                securityTrusteeRepository.save(securityTrustee);
            }
            size--;
        }
    }
}
