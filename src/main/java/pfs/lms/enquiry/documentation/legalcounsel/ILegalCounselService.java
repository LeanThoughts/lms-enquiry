package pfs.lms.enquiry.documentation.legalcounsel;

import java.util.UUID;

public interface ILegalCounselService {

    LegalCounsel create(LegalCounselResource resource, String username);

    LegalCounsel update(LegalCounselResource resource, String username) throws CloneNotSupportedException;

    LegalCounsel delete(UUID legalCounselId, String username) throws CloneNotSupportedException;
}
