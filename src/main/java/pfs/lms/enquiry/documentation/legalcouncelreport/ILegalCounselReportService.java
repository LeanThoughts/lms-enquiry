package pfs.lms.enquiry.documentation.legalcouncelreport;

import java.util.UUID;

public interface ILegalCounselReportService {

    LegalCounselReport create(LegalCounselReportResource resource, String username);

    LegalCounselReport update(LegalCounselReportResource resource, String username) throws CloneNotSupportedException;

    LegalCounselReport delete(UUID legalCounselId, String username) throws CloneNotSupportedException;
}
