package pfs.lms.enquiry.documentation.legalcounselfee;

import java.util.UUID;

public interface ILegalCounselFeeService {

    LegalCounselFee create(LegalCounselFeeResource resource, String username);

    LegalCounselFee update(LegalCounselFeeResource resource, String username) throws CloneNotSupportedException;

    LegalCounselFee delete(UUID legalCounselFeeId, String username) throws CloneNotSupportedException;
}
