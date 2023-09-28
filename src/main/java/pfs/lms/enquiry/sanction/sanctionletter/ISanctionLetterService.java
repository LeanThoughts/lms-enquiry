package pfs.lms.enquiry.sanction.sanctionletter;

import java.util.UUID;

public interface ISanctionLetterService {

    SanctionLetter create(SanctionLetterResource sanctionLetterResource, String username);

    SanctionLetter update(SanctionLetterResource sanctionLetterResource, String username)
            throws CloneNotSupportedException;

    SanctionLetter delete(UUID sanctionLetterId) throws CloneNotSupportedException;
}
