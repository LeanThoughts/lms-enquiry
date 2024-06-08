package pfs.lms.enquiry.enquiriesexcelupload;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExcelEnquiryRepository extends JpaRepository<ExcelEnquiry, UUID> {
}
