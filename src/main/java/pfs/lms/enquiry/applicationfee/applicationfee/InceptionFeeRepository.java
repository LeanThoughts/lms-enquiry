package pfs.lms.enquiry.applicationfee.applicationfee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InceptionFeeRepository extends JpaRepository<InceptionFee, UUID> {

    List<InceptionFee> findByApplicationFeeId(UUID applicationFeeId);
    List<InceptionFee> findByInvoiceNumber(String invoiceNumber);
    List<InceptionFee> findByHeaderDocumentNumber(String headerDocumentNumber);
    List<InceptionFee> findBySapFIDocumentNumberFee(String fiDocumentNumber);
}
