package pfs.lms.enquiry.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFee;
import pfs.lms.enquiry.monitoring.lie.LendersIndependentEngineer;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFeeRepository;
import pfs.lms.enquiry.monitoring.lie.LIERepository;
import pfs.lms.enquiry.repository.LoanApplicationRepository;
import pfs.lms.enquiry.monitoring.repository.LoanMonitorRepository;
import pfs.lms.enquiry.monitoring.lie.LIEReportAndFeeResource;
import pfs.lms.enquiry.service.ILIEReportAndFeeService;
import pfs.lms.enquiry.service.changedocs.IChangeDocumentService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LIEReportAndFeeService implements ILIEReportAndFeeService{

    private final LIERepository lieRepository;
    private final LIEReportAndFeeRepository lieReportAndFeeRepository;
    private  final LoanMonitorRepository loanMonitorRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private final IChangeDocumentService changeDocumentService;

    @Override
    @Transactional
    public LIEReportAndFee save(LIEReportAndFeeResource resource, String username) {


        LendersIndependentEngineer lendersIndependentEngineer = lieRepository.getOne(resource.getLendersIndependentEngineerId());
        LIEReportAndFee lieReportAndFee = resource.getLieReportAndFee();
        lieReportAndFee.setLendersIndependentEngineer(lendersIndependentEngineer);
        lieReportAndFee = lieReportAndFeeRepository.save(lieReportAndFee);

        //Create Change Document
        changeDocumentService.createChangeDocument(
                lieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getId() ,
                lieReportAndFee.getId(), lendersIndependentEngineer.getId(),
                lieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
                null,
                lieReportAndFee,
                "Created",
                username,
                "Monitoring", "LIE Report And Fee" );

        return lieReportAndFee;
    }

    @Override
    public LIEReportAndFee update(LIEReportAndFeeResource resource, String username) {

        LIEReportAndFee existinglieReportAndFee
                = lieReportAndFeeRepository.getOne(resource.getLieReportAndFee().getId());

        existinglieReportAndFee.setReportType(resource.getLieReportAndFee().getReportType());
        existinglieReportAndFee.setDateOfReceipt(resource.getLieReportAndFee().getDateOfReceipt());
        existinglieReportAndFee.setInvoiceDate(resource.getLieReportAndFee().getInvoiceDate());
        existinglieReportAndFee.setInvoiceNo(resource.getLieReportAndFee().getInvoiceNo());
        existinglieReportAndFee.setFeeAmount(resource.getLieReportAndFee().getFeeAmount());
        existinglieReportAndFee.setStatusOfFeeReceipt(resource.getLieReportAndFee().getStatusOfFeeReceipt());
        existinglieReportAndFee.setStatusOfFeePaid(resource.getLieReportAndFee().getStatusOfFeePaid());
        existinglieReportAndFee.setDocumentTitle(resource.getLieReportAndFee().getDocumentTitle());
        existinglieReportAndFee.setNextReportDate(resource.getLieReportAndFee().getNextReportDate());
        existinglieReportAndFee = lieReportAndFeeRepository.save(existinglieReportAndFee);

        //Create Change Document
        changeDocumentService.createChangeDocument(
                existinglieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getId() ,
                existinglieReportAndFee.getId(), existinglieReportAndFee.getLendersIndependentEngineer().getId(),
                existinglieReportAndFee.getLendersIndependentEngineer().getLoanMonitor().getLoanApplication().getLoanContractId(),
                existinglieReportAndFee,
                resource.getLieReportAndFee(),
                "Updated",
                username,
                "Monitoring", "LIE Report And Fee" );


        return existinglieReportAndFee;
    }

    @Override
    public List<LIEReportAndFeeResource> getLIEReportAndFee(String lendersIndependentEngineerId, String name) {

        List<LIEReportAndFeeResource>  lieReportAndFeeResources  = new ArrayList<>();
        LendersIndependentEngineer lendersIndependentEngineer = lieRepository.getOne(lendersIndependentEngineerId);
        //LoanMonitor loanMonitor = loanMonitorRepository.findByLoanApplication(loanApplication);
        if(lendersIndependentEngineer != null) {
            List<LIEReportAndFee> lieReportAndFees
                    = lieReportAndFeeRepository.findByLendersIndependentEngineer(lendersIndependentEngineer);
            lieReportAndFees.forEach(
                    lieReportAndFee -> {
                        LIEReportAndFeeResource lieReportAndFeeResource = new LIEReportAndFeeResource();
                        //lieResource.setLoanApplicationId(loanApplication.getId());
                        lieReportAndFeeResource.setLendersIndependentEngineerId(lendersIndependentEngineer.getId());
                        lieReportAndFeeResource.setLieReportAndFee(lieReportAndFee);
                        lieReportAndFeeResources.add(lieReportAndFeeResource);
                    }
            );
        }
        return lieReportAndFeeResources;
    }
}
