package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.domain.LoanApplication;
import pfs.lms.enquiry.monitoring.domain.LoanMonitor;
import pfs.lms.enquiry.monitoring.lie.LIERepository;

import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.lie.LIEResource;
import pfs.lms.enquiry.service.ILIEService;
import pfs.lms.enquiry.service.changedocs.impl.ChangeDocumentService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LIEService implements ILIEService {

    private final LIERepository lieRepository;
    private  final LoanMonitorRepository loanMonitorRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final ChangeDocumentService changeDocumentService;

    @Override
    @Transactional
    public LendersIndependentEngineer save(LIEResource resource, String username) {

        LoanApplication loanApplication = loanApplicationRepository.getOne(resource.getLoanApplicationId());

        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor == null)
        {
            loanMonitor = new LoanMonitor();
            loanMonitor.setLoanApplication(loanApplication);
            loanMonitor = loanMonitorRepository.save(loanMonitor);
        }
        // Change Documents for Monitoring Header
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(),
                loanMonitor.getId().toString(),null,
                loanApplication.getLoanContractId(),
                null,
                loanMonitor,
                "Created",
                username,
                "Monitoring", "Header");


        LendersIndependentEngineer lendersIndependentEngineer = resource.getLendersIndependentEngineer();
        lendersIndependentEngineer.setLoanMonitor(loanMonitor);
        lendersIndependentEngineer.setAdvisor(resource.getLendersIndependentEngineer().getAdvisor());
        lendersIndependentEngineer.setBpCode(resource.getLendersIndependentEngineer().getBpCode());
        lendersIndependentEngineer.setName(resource.getLendersIndependentEngineer().getName());
        lendersIndependentEngineer.setDateOfAppointment(resource.getLendersIndependentEngineer().getDateOfAppointment());
        lendersIndependentEngineer.setContractPeriodFrom(resource.getLendersIndependentEngineer().getContractPeriodFrom());
        lendersIndependentEngineer.setContractPeriodTo(resource.getLendersIndependentEngineer().getContractPeriodTo());
        lendersIndependentEngineer.setContactPerson(resource.getLendersIndependentEngineer().getContactPerson());
        lendersIndependentEngineer.setContactNumber(resource.getLendersIndependentEngineer().getContactNumber());
        lendersIndependentEngineer.setEmail(resource.getLendersIndependentEngineer().getEmail());
        lendersIndependentEngineer = lieRepository.save(lendersIndependentEngineer);

        //Create Change Document
        //Create Change Document
        changeDocumentService.createChangeDocument(
                loanMonitor.getId(), lendersIndependentEngineer.getId(),null,
                lendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                lendersIndependentEngineer,
                "Created",
                username,
                "Monitoring", "Lenders Independent Engineer" );

        return lendersIndependentEngineer;




    }

    @Override
    public LendersIndependentEngineer update(LIEResource resource, String username) {

        LendersIndependentEngineer existingLendersIndependentEngineer
                = lieRepository.getOne(resource.getLendersIndependentEngineer().getId());

        existingLendersIndependentEngineer.setAdvisor(resource.getLendersIndependentEngineer().getAdvisor());
        existingLendersIndependentEngineer.setBpCode(resource.getLendersIndependentEngineer().getBpCode());
        existingLendersIndependentEngineer.setName(resource.getLendersIndependentEngineer().getName());
        existingLendersIndependentEngineer.setDateOfAppointment(resource.getLendersIndependentEngineer().getDateOfAppointment());
        existingLendersIndependentEngineer.setContractPeriodFrom(resource.getLendersIndependentEngineer().getContractPeriodFrom());
        existingLendersIndependentEngineer.setContractPeriodTo(resource.getLendersIndependentEngineer().getContractPeriodTo());
        existingLendersIndependentEngineer.setContactPerson(resource.getLendersIndependentEngineer().getContactPerson());
        existingLendersIndependentEngineer.setContactNumber(resource.getLendersIndependentEngineer().getContactNumber());
        existingLendersIndependentEngineer.setEmail(resource.getLendersIndependentEngineer().getEmail());
        existingLendersIndependentEngineer = lieRepository.save(existingLendersIndependentEngineer);

        changeDocumentService.createChangeDocument(
                existingLendersIndependentEngineer.getLoanMonitor().getId(),
                existingLendersIndependentEngineer.getId(), null,
                existingLendersIndependentEngineer.getLoanMonitor().getLoanApplication().getLoanContractId(),
                existingLendersIndependentEngineer,
                resource.getLendersIndependentEngineer(),
                "Updated",
                username,
                "Monitoring", "Lenders Independent Engineer" );



        return existingLendersIndependentEngineer;
    }

    @Override
    public List<LIEResource> getLendersIndependentEngineers(String loanApplicationId, String name) {

        List<LIEResource> lendersIndependentEngineerResources = new ArrayList<>();
        LoanApplication loanApplication = loanApplicationRepository.getOne(UUID.fromString(loanApplicationId));
        LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(loanMonitor != null) {
            List<LendersIndependentEngineer> lendersIndependentEngineers
                    = lieRepository.findByLoanMonitor(loanMonitor);
            lendersIndependentEngineers.forEach(
                    lendersIndependentEngineer -> {
                        LIEResource lieResource = new LIEResource();
                        lieResource.setLoanApplicationId(loanApplication.getId());
                        lieResource.setLendersIndependentEngineer(lendersIndependentEngineer);
                        lendersIndependentEngineerResources.add(lieResource);
                    }
            );
        }
        return lendersIndependentEngineerResources;
    }
}
