package pfs.lms.enquiry.applicationfee.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoicingDetailRepository extends JpaRepository<InvoicingDetail, UUID> {

    InvoicingDetail findByApplicationFeeId(UUID applicationFeeId);
}
