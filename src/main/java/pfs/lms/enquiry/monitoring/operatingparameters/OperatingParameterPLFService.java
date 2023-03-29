package pfs.lms.enquiry.monitoring.operatingparameters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.service.changedocs.impl.ChangeDocumentService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperatingParameterPLFService implements IOperatingParameterPLFService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanMonitorRepository loanMonitorRepository;
    private final ChangeDocumentService changeDocumentService;
    private final OperatingParameterPLFRepository operatingParameterPLFRepository;

    @Override
    public OperatingParameterPLF saveOperatingParameterPLF(OperatingParameterPLFResource resource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);

            changeDocumentService.createChangeDocument(
                    loanMonitor.getId(),
                    loanMonitor.getId().toString(),null,
                    loanApplication.getLoanContractId(),
                    null,
                    loanMonitor,
                    "Created",
                    username,
                    "Monitoring", "Header" );
        }
        OperatingParameterPLF operatingParameterPLF = resource.getOperatingParameterPLF();
        operatingParameterPLF.setLoanMonitor(loanMonitor);
        operatingParameterPLF.setSerialNumber(operatingParameterPLFRepository.findByLoanMonitor(loanMonitor).size() + 1);
        operatingParameterPLF = operatingParameterPLFRepository.save(operatingParameterPLF);

        changeDocumentService.createChangeDocument(
                loanMonitor.getId() ,
                operatingParameterPLF.getId(),null,
                loanApplication.getLoanContractId(),
                null,
                operatingParameterPLF,
                "Created",
                username,
                "Monitoring", "Operating Parameter PLF" );

        return operatingParameterPLF;

    }

    @Override
    public OperatingParameterPLF updateOperatingParameterPLF(OperatingParameterPLFResource resource, String username)
            throws CloneNotSupportedException {
        OperatingParameterPLF existingOperatingParameterPLF
                = operatingParameterPLFRepository.getOne(resource.getOperatingParameterPLF().getId());

        Object oldExistingOperatingParameter = existingOperatingParameterPLF.clone();

        existingOperatingParameterPLF.setSerialNumber(resource.getOperatingParameterPLF().getSerialNumber());
        existingOperatingParameterPLF.setYear(resource.getOperatingParameterPLF().getYear());
        existingOperatingParameterPLF.setRemarks(resource.getOperatingParameterPLF().getRemarks());
        existingOperatingParameterPLF.setDesignPlfCuf(resource.getOperatingParameterPLF().getDesignPlfCuf());
        existingOperatingParameterPLF.setMiUnGenerated(resource.getOperatingParameterPLF().getMiUnGenerated());
        existingOperatingParameterPLF.setActualYearlyAveragePlfCuf(resource.getOperatingParameterPLF().getActualYearlyAveragePlfCuf());
        existingOperatingParameterPLF = operatingParameterPLFRepository.save(existingOperatingParameterPLF);

        // Change Documents for Operating Parameter
        changeDocumentService.createChangeDocument(
                existingOperatingParameterPLF.getLoanMonitor().getId(),
                existingOperatingParameterPLF.getId(),null,
                existingOperatingParameterPLF.getLoanMonitor().getLoanApplication().getLoanContractId(),
                oldExistingOperatingParameter,
                existingOperatingParameterPLF,
                "Updated",
                username,
                "Monitoring", "Operating Parameter PLF");

        return existingOperatingParameterPLF;
    }

    @Override
    public List<OperatingParameterPLFResource> getOperatingParameterPLF(String loanApplicationId, String name) {
        List<OperatingParameterPLFResource> operatingParameterPLFResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<OperatingParameterPLF> operatingParameterPLFs
                    = operatingParameterPLFRepository.findByLoanMonitor(loanMonitor);
            operatingParameterPLFs.forEach(
                    operatingParameterPLF -> {
                        OperatingParameterPLFResource operatingParameterPLFResource = new OperatingParameterPLFResource();
                        operatingParameterPLFResource.setLoanApplicationId(loanApplication.getId());
                        operatingParameterPLFResource.setOperatingParameterPLF(operatingParameterPLF);
                        operatingParameterPLFResources.add(operatingParameterPLFResource);
                    }
            );
        }
        Collections.sort(operatingParameterPLFResources, Comparator.comparingInt((OperatingParameterPLFResource a) ->
                a.getOperatingParameterPLF().getSerialNumber()).reversed());
        return operatingParameterPLFResources;
    }

    @Override
    public OperatingParameterPLF deleteOperatingParameterPLF(UUID operatingParameterId, String username) {
        OperatingParameterPLF operatingParameterPLF = operatingParameterPLFRepository.
                getOne(operatingParameterId.toString());
        LoanMonitor loanMonitor = operatingParameterPLF.getLoanMonitor();

//        UUID loanBusinessProcessObjectId = this.getLoanBusinessProcessObjectId(operatingParameter.getLoanMonitor(),
//                operatingParameter.getLoanAppraisal(), moduleName);
//
//        // Create Change Document for LIE Delete
//        changeDocumentService.createChangeDocument(
//                loanBusinessProcessObjectId,
//                operatingParameter.getId(),
//                null,
//                operatingParameter.getLoanMonitor().getLoanApplication().getLoanContractId(),
//                null,
//                operatingParameter,
//                "Deleted",
//                username,
//                moduleName, "Lenders Independent Engineer" );

        operatingParameterPLFRepository.delete(operatingParameterPLF);
        updateOperatingParameterPLFSerialNumbers(loanMonitor);

        return operatingParameterPLF;
    }

    private void updateOperatingParameterPLFSerialNumbers(LoanMonitor loanMonitor) {
        List<OperatingParameterPLF> operatingParameterPLFList = operatingParameterPLFRepository.findByLoanMonitor(loanMonitor);
        int size = operatingParameterPLFList.size();
        for(OperatingParameterPLF operatingParameterPLF: operatingParameterPLFList) {
            if (operatingParameterPLF.getSerialNumber() != size) {
                operatingParameterPLF.setSerialNumber(size);
                operatingParameterPLFRepository.save(operatingParameterPLF);
            }
            size--;
        }
    }
}
