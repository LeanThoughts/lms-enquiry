package pfs.lms.enquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.EnquiryNo;
import pfs.lms.enquiry.domain.LoanApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {

    Page<LoanApplication> findByFunctionalStatus(Integer status, Pageable pageable);

    Page<LoanApplication> findByLoanApplicant(UUID id, Pageable pageable);

    Page<LoanApplication> findByLoanApplicantAndFunctionalStatus(UUID id, Integer status, Pageable pageable);

    LoanApplication findByLoanEnquiryId(Long enquiryNo);

    LoanApplication findByEnquiryNo(EnquiryNo enquiryNo);

    LoanApplication findByLoanContractId(String loanContractId);

    List<LoanApplication> findByTechnicalStatusAndPostedInSAP(Integer technicalStatus, Integer postedInSAP);

    List<LoanApplication> findByTechnicalStatusAndPostedInSAPLessThan(Integer technicalStatus, Integer postedInSAP);

    List<LoanApplication> findByProjectTypeAndAssistanceTypeAndProposalTypeAndLoanContractAmountAndLoanEnquiryDate
            (String projectType, String assistanceType, String proposalType, Double loanContractAmount, LocalDate enquiryDate);
}