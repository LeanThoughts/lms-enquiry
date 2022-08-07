package pfs.lms.enquiry.appraisal.loanpartner;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.LoanApplication;

import java.util.List;
import java.util.UUID;

public interface LoanPartnerRepository extends JpaRepository<LoanPartner, UUID> {

    List<LoanPartner> findByLoanApplicationIdOrderBySerialNumberDesc(UUID loanApplicationId);

    List<LoanPartner> findByLoanApplicationIdAndKycRequired(UUID loanApplicationId, boolean kycRequired);

    LoanPartner findByLoanApplicationAndBusinessPartnerId(LoanApplication loanApplication , String businessPartnerId);

    LoanPartner findByLoanApplicationAndBusinessPartnerIdAndRoleType(LoanApplication loanApplication,String businessPartnerId, String roleType);

    List<LoanPartner> findByLoanApplicationIdAndRoleType(UUID loanApplicationId, String roleType);

    List<LoanPartner> findByLoanAppraisalId(String appraisalId);
}
