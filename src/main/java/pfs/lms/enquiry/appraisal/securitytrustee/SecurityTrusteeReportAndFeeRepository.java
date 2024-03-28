package pfs.lms.enquiry.appraisal.securitytrustee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityTrusteeReportAndFeeRepository extends JpaRepository<SecurityTrusteeReportAndFee, String> {

    List<SecurityTrusteeReportAndFee> findBySecurityTrustee(SecurityTrustee securityTrustee);

    List<SecurityTrusteeReportAndFee> findBySecurityTrusteeOrderBySerialNumberDesc(SecurityTrustee securityTrustee);
}
