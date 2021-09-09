package pfs.lms.enquiry.appraisal.syndicateconsortium;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.appraisal.loanappraisalkyc.LoanAppraisalKYC;

import java.util.UUID;


public interface BankRepository extends JpaRepository<LoanAppraisalKYC, UUID> {

}
