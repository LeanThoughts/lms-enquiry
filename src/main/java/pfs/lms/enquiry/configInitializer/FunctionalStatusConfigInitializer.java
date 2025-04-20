package pfs.lms.enquiry.configInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pfs.lms.enquiry.domain.*;
import pfs.lms.enquiry.referenceinterest.domain.ReferenceInterestRate;
import pfs.lms.enquiry.referenceinterest.repository.ReferenceInterestRateRepository;
import pfs.lms.enquiry.repository.*;

/**
 * Created by sajeev on 14-May-21.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FunctionalStatusConfigInitializer implements CommandLineRunner {


    private final FunctionalStatusRepository functionalStatusRepository;

    @Override
    public void run(String... strings) throws Exception {

        /**
         * 01-Enquiry Stage
         * 02-ICC In-Principle Approved
         * 03-Appraisal Stage
         * 04-Board Approval Stage
         * 05-Sanction Stage
         * 06-Loan Documentation Stage
         * 07-Loan Disbursement Stage -
         * 08-Monitoring
         * 09-Recovery
         * 10-Preliminary Risk Assessment Completed
         * 11-Application Fee
         * 12-BMC Approval
         * 80 - Cancelled
         * 90- Planned Completed
         * 99- Actual Completed
         */
        
        FunctionalStatus functionalStatus = functionalStatusRepository.findByCode(1);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(1, "Enquiry Stage");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(2);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(2, "ICC In-Principle Approved");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(3);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(3, "Appraisal Stage");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(4);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(4, "Board Approval Stage");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(5);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(5, "Sanction Stage");
            functionalStatusRepository.save(functionalStatus);
        }
        
        functionalStatus = functionalStatusRepository.findByCode(6);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(6, "Loan Documentation Stage");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(7);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(7, "Loan Disbursement Stage");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(8);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(8, "Monitoring");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(9);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(9, "Recovery");
            functionalStatusRepository.save(functionalStatus);
        }

        functionalStatus = functionalStatusRepository.findByCode(10);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(10, "Preliminary Risk Assessment Completed");
            functionalStatusRepository.save(functionalStatus);
        }
        
        functionalStatus = functionalStatusRepository.findByCode(11);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(11, "Application Fee");
            functionalStatusRepository.save(functionalStatus);
        }
        
        functionalStatus = functionalStatusRepository.findByCode(12);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(12, "BMC Approval");
            functionalStatusRepository.save(functionalStatus);
        }
        
        functionalStatus = functionalStatusRepository.findByCode(80);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(80, "Cancelled");
            functionalStatusRepository.save(functionalStatus);
        }
        
        functionalStatus = functionalStatusRepository.findByCode(90);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(90, "Planned Completed");
            functionalStatusRepository.save(functionalStatus);
        }
        
        functionalStatus = functionalStatusRepository.findByCode(99);
        if (functionalStatus == null) {
            functionalStatus = new FunctionalStatus(99, "Actual Completed");
            functionalStatusRepository.save(functionalStatus);
        }
    }
}
